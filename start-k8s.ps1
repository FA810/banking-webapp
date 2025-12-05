# =================================================================
# Impostazioni Iniziali
# =================================================================

# Permette l'esecuzione dello script (se necessario)
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass -Force

# --- CONFIGURAZIONE VARIABILI ---
# Importante: Le immagini devono usare il formato standard di Docker (es. 'repo/immagine:tag')
$ClusterName = "banking-cluster"

# Immagini usate
$IMAGE_POSTGRES = "postgres:15"
$IMAGE_KAFKA = "apache/kafka:3.9.1" # Immagine standard per KRaft
$IMAGE_WEBAPP = "bankingwebapp:latest"

# Nomi dei file YAML (Aggiornati)
$YAML_POSTGRES = "postgres.yaml"
# Utilizziamo il nuovo manifest Kafka KRaft con StatefulSet
$YAML_KAFKA = "kafka-kraft-statefulset.yaml" 
$YAML_WEBAPP = "bankingwebapp.yaml"

Write-Host "Reset completo del cluster '$ClusterName' e pulizia cache Docker potenziata..." -ForegroundColor Cyan

# =================================================================
# 1. SETUP AMBIENTE KIND E PULIZIA
# =================================================================

# 1a. Eliminazione del cluster esistente
Write-Host "Eliminazione del cluster Kind esistente..."
# Reindirizza l'output a NULL per sopprimere i messaggi non essenziali
kind delete cluster --name $ClusterName | Out-Null
Start-Sleep -Seconds 5

# 1b. Pulizia Aggressiva di Docker
Write-Host "Rimozione delle immagini locali e pulizia della cache Docker..."
$OriginalErrorActionPreference = $ErrorActionPreference
$ErrorActionPreference = "SilentlyContinue"

# Pulizia di Docker per rimuovere layer inattivi
docker system prune -f | Out-Null
docker volume prune -f | Out-Null

# Tentativi di rimozione immagine (Ignora errori se non esistono)
docker rmi $IMAGE_KAFKA
docker rmi $IMAGE_POSTGRES
docker rmi $IMAGE_WEBAPP
# Rimuovi eventuali vecchie immagini (Bitnami/Confluent)
docker rmi "bitnami/zookeeper:3.9.1"
docker rmi "bitnami/kafka:3.5.2"

$ErrorActionPreference = $OriginalErrorActionPreference
Write-Host "Pulizia immagini e cache completata." -ForegroundColor Yellow

# 1c. Ricreazione del nuovo cluster multi-nodo
Write-Host "Creazione del cluster '$ClusterName' con configurazione multi-nodo..." -ForegroundColor Green

# Definizione del contenuto YAML con una Here-String
$KindConfig = @"
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
- role: worker
- role: worker
"@

# Scrive il contenuto nel file kind-config.yaml
$KindConfig | Set-Content -Path kind-config.yaml

kind create cluster --config kind-config.yaml --name $ClusterName

Write-Host "Cluster Kind pronto." -ForegroundColor Green

# =================================================================
# 2. PULL E BUILD DELLE IMMAGINI DOCKER
# =================================================================

# Esegui la build dell'immagine locale
Write-Host "Build dell'immagine locale $IMAGE_WEBAPP..." -ForegroundColor Green
docker build --no-cache -t $IMAGE_WEBAPP .

Write-Host "Pull immagini esterne (saranno riscaricate se necessario)..." -ForegroundColor Green
# Pull immagini Kafka e Postgres
docker pull $IMAGE_KAFKA
docker pull $IMAGE_POSTGRES

# =================================================================
# 3. CARICAMENTO IMMAGINI NEL CLUSTER KIND
# =================================================================

Write-Host "Caricamento immagini Docker nel cluster Kind..." -ForegroundColor Green

# Caricamento delle immagini nel cluster
kind load docker-image $IMAGE_KAFKA --name $ClusterName
kind load docker-image $IMAGE_POSTGRES --name $ClusterName
kind load docker-image $IMAGE_WEBAPP --name $ClusterName

# =================================================================
# 4. APPLICAZIONE DEI MANIFEST KUBERNETES
# =================================================================

Write-Host "Inizio applicazione manifest in ordine: Postgres -> Kafka (KRaft) -> Web App" -ForegroundColor Green

# 1. Postgres (Database)
kubectl apply -f $YAML_POSTGRES

# 2. Kafka (Messaging) - NUOVO FILE KRaft StatefulSet
kubectl apply -f $YAML_KAFKA

# 3. Banking Web App (Applicazione) - Assicurati che questo file sia aggiornato!
kubectl apply -f $YAML_WEBAPP

Write-Host "Setup completato. Controlla lo stato dei Pod con: kubectl get pods -A" -ForegroundColor Cyan