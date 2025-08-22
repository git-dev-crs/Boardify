## Service Registry - Boardify

This is the **Service Registry** module of the **Boardify** project, responsible for enabling **service discovery** using **Netflix Eureka Server**. All microservices in the Boardify system register themselves with this registry to allow for dynamic discovery and communication.

---

### Responsibilities

- Acts as a Eureka Server for service registration and discovery.
- Keeps track of available microservices and their instance metadata.
- Enables load balancing and failover through service-aware routing (via Gateway).

---

### Folder Structure
```text
service-registry/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.boardify.service_registry/
│   │   │       └── ServiceRegistryApplication.java      # Main Spring Boot application entry point
│   └── resources/
│       └── application.properties                      # Spring Boot configuration file
├── test/                                               # Unit and integration tests
├── pom.xml                                             # Maven build configuration
└── README.md                                           # Project documentation
```

---

### How to run on your machine

1. Port Number: _8761_
2. Application Name: _service-registry_

---
