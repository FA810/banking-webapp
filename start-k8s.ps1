# =================================================================
# Initial Settings
# =================================================================

# Allows script execution (if necessary)
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass -Force

# --- VARIABLE CONFIGURATION ---
# Important: Images must use the standard Docker format (e.g., 'repo/image:tag')
$ClusterName = "banking-cluster"

# Images used
$IMAGE_POSTGRES = "postgres:15"
$IMAGE_KAFKA = "apache/kafka:3.9.1" # Standard image for KRaft
$IMAGE_WEBAPP = "bankingwebapp:latest"

# YAML file names (Updated)
$YAML_POSTGRES = "postgres.yaml"
# We use the new Kafka KRaft manifest with StatefulSet
$YAML_KAFKA = "kafka-kraft-statefulset.yaml" 
$YAML_WEBAPP = "bankingwebapp.yaml"

Write-Host "Complete reset of cluster '$ClusterName' and enhanced Docker cache cleanup..." -ForegroundColor Cyan

# =================================================================
# 1. KIND ENVIRONMENT SETUP AND CLEANUP
# =================================================================

# 1a. Deletion of existing cluster
Write-Host "Deleting existing Kind cluster..."
# Redirect output to NULL to suppress non-essential messages
kind delete cluster --name $ClusterName | Out-Null
Start-Sleep -Seconds 5

# 1b. Aggressive Docker Cleanup
Write-Host "Removing local images and cleaning Docker cache..."
$OriginalErrorActionPreference = $ErrorActionPreference
$ErrorActionPreference = "SilentlyContinue"

# Docker cleanup to remove inactive layers
docker system prune -f | Out-Null
docker volume prune -f | Out-Null

# Attempt to remove images (Ignore errors if they don't exist)
docker rmi $IMAGE_KAFKA
docker rmi $IMAGE_POSTGRES
docker rmi $IMAGE_WEBAPP
# Remove any old images (Bitnami/Confluent)
docker rmi "bitnami/zookeeper:3.9.1"
docker rmi "bitnami/kafka:3.5.2"

$ErrorActionPreference = $OriginalErrorActionPreference
Write-Host "Image and cache cleanup completed." -ForegroundColor Yellow

# 1c. Recreate the new multi-node cluster
Write-Host "Creating cluster '$ClusterName' with multi-node configuration..." -ForegroundColor Green

# Define the YAML content using a Here-String
$KindConfig = @"
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
- role: worker
- role: worker
"@

# Write the content to the kind-config.yaml file
$KindConfig | Set-Content -Path kind-config.yaml

kind create cluster --config kind-config.yaml --name $ClusterName

Write-Host "Kind cluster ready." -ForegroundColor Green

# =================================================================
# 2. PULL AND BUILD DOCKER IMAGES
# =================================================================

# Execute local image build
Write-Host "Building local image $IMAGE_WEBAPP..." -ForegroundColor Green
docker build --no-cache -t $IMAGE_WEBAPP .

Write-Host "Pulling external images (will be redownloaded if necessary)..." -ForegroundColor Green
# Pull Kafka and Postgres images
docker pull $IMAGE_KAFKA
docker pull $IMAGE_POSTGRES

# =================================================================
# 3. LOAD IMAGES INTO KIND CLUSTER
# =================================================================

Write-Host "Loading Docker images into Kind cluster..." -ForegroundColor Green

# Loading images into the cluster
kind load docker-image $IMAGE_KAFKA --name $ClusterName
kind load docker-image $IMAGE_POSTGRES --name $ClusterName
kind load docker-image $IMAGE_WEBAPP --name $ClusterName

# =================================================================
# 4. APPLY KUBERNETES MANIFESTS
# =================================================================

Write-Host "Starting manifest application in order: Postgres -> Kafka (KRaft) -> Web App" -ForegroundColor Green

# 1. Postgres (Database)
kubectl apply -f $YAML_POSTGRES

# 2. Kafka (Messaging) - NEW KRaft StatefulSet file
kubectl apply -f $YAML_KAFKA

# 3. Banking Web App (Application) - Ensure this file is up to date!
kubectl apply -f $YAML_WEBAPP

Write-Host "Setup complete. Check Pod status with: kubectl get pods -A" -ForegroundColor Cyan