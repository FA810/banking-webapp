How to **quickly** start:

```
docker compose -f infra-compose.yml -f app-compose.yml up --build -d
```



# Intro

Banking-WebApp is a demo online banking system with user, account, and transaction features. It runs PostgreSQL and Kafka in Docker for persistence and async workflows. Built with Java + Spring, it showcases containerized, event-driven banking operations for learning and experimentation.



# 🚀 Getting Started

This guide explains how to deploy and run the **Banking Web Application** along with its dependencies (PostgreSQL and Kafka) using either Docker Compose for local development or Kubernetes (Kind) for a production-like environment.

---

## 💻 1. Local Deployment with Docker Compose

This method is recommended for quick local development and testing.

### Prerequisites

You must have **Docker** and **Docker Compose (V2)** installed and running on your system.

### 1.1 Project Structure

The project uses two separate Docker Compose files:

- **`infra-compose.yml`**: Defines the shared infrastructure services (postgres db, kafka-kraft, ...).

- **`app-compose.yml`**: Defines the main application (`banking-app`).

### 1.2 Start the Application

Execute the following command from the root directory of the project. This command builds the application image and starts all services in detached mode (`-d`).

Bash

```
docker compose -f infra-compose.yml -f app-compose.yml up --build -d
```

### 1.3 Verify Services

After a few moments, all services should be running.

| **Service**     | **Address**             | **Description**                                      |
| --------------- | ----------------------- | ---------------------------------------------------- |
| **Banking App** | `http://localhost:8080` | The main application interface.                      |
| **Kafka UI**    | `http://localhost:8081` | Web interface for viewing Kafka topics and messages. |
| **PostgreSQL**  | `localhost:5432`        | Database endpoint (internal access).                 |

### 1.4 Stop the Application

To stop and remove all containers, networks, and volumes defined in the Compose files:

Bash

```
docker compose -f infra-compose.yml -f app-compose.yml down -v
```

---

## That's a very important detail! Knowing that the **`start-k8s.ps1` script handles the creation of the Kind cluster** simplifies the prerequisites significantly for the user.

I'll update the Kubernetes section of your README to reflect that the cluster setup is managed by the script itself.

Here is the final, corrected "Getting Started" section for your GitHub README:

# 🚀 Getting Started

This guide explains how to deploy and run the **Banking Web Application** along with its dependencies (PostgreSQL and Kafka) using either Docker Compose for local development or Kubernetes for a production-like environment.

---

## 💻 1. Local Deployment with Docker Compose

This method is recommended for quick local development and testing.

### Prerequisites

You must have **Docker** and **Docker Compose (V2)** installed and running on your system.

### 1.1 Project Structure

The project uses two separate Docker Compose files:

- **`infra-compose.yml`**: Defines the shared infrastructure services (`db`, `kafka-kraft`, `kafka-ui`).

- **`app-compose.yml`**: Defines the main application (`banking-app`).

### 1.2 Start the Application

Execute the following command from the root directory of the project. This command builds the application image and starts all services in detached mode (`-d`).

Bash

```
docker compose -f infra-compose.yml -f app-compose.yml up --build -d
```

### 1.3 Verify Services

After a few moments, all services should be running.

| **Service**     | **Address**             | **Description**                                      |
| --------------- | ----------------------- | ---------------------------------------------------- |
| **Banking App** | `http://localhost:8080` | The main application interface.                      |
| **Kafka UI**    | `http://localhost:8081` | Web interface for viewing Kafka topics and messages. |
| **PostgreSQL**  | `localhost:5432`        | Database endpoint (internal access).                 |

### 1.4 Stop the Application

To stop and remove all containers, networks, and volumes defined in the Compose files:

Bash

```
docker compose -f infra-compose.yml -f app-compose.yml down -v
```

---

## ☁️ 2. Deployment on Kubernetes (using Kind)

This method uses the **Kind** tool to create a local Kubernetes cluster and deploy the application stack.

### Prerequisites

1. **Docker** must be installed and running (Kind runs the cluster inside Docker containers).

### 2.1 Start the Cluster and Deploy

To deploy all components, execute the provided PowerShell deployment script. This script will:

1. **Create the Kind cluster.**

2. Build and load the necessary Docker images into the cluster.

3. Apply all Kubernetes manifests.

In PowerShell:

```
.\start-k8s.ps1
```

### 2.2 Verify Deployment Status

Monitor the status of all pods until they show **`1/1 Running`** with stable restart counts.



```
kubectl get pods
```

### 2.3 Access the Application

Once all pods are running, use port-forwarding to access the application service locally.

Forward port 8080 from the application service to your local machine

```
kubectl port-forward service/bankingwebapp-service 8080:8080
```

You can now access the application at **`http://localhost:8080`**.
