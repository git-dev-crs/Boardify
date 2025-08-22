## Boardify

### Overview

Boardify is a microservices-based project management backend system inspired by tools like Trello, built using Spring
Boot and designed for scalability, modularity, and team collaboration. <br>
It allows users to create workspaces, boards, lists, manage tasks, assign team members, and track progress in real-time.

---

### Technologies Used

- Java 17
- Spring Boot 3.3.3
- Spring Boot Web 3.4.2
- MySQL Database
- Spring Boot JPA 3.4.3
- Netflix Eureka Client 4.0.3
- Open Feign 4.1.3
- Lombok 1.18.30

---

### Services

1. **Profile Service** - Manages user profile data. [_README_](./profile-service/README.md)
2. **Workspace Service** - Manage workspace-related functionality. [_README_](./workspace-service/README.md)
3. **Board Service** - Manage board-related functionality. [_README_](./board-service/README.md)
4. **List Service** - Manage list-related functionality. [_README_](./list-service/README.md)
5. **Card Service** - Manage card-related functionality. [_README_](./card-service/README.md)
6. **Service Registry** - Eureka Service Registry. [_README_](./service-registry/README.md)
7. **Api Gateway** - Api Gateway Service. [_README_](./api-interface/README.md)

---

### Folder structure
```text
Boardify
    |- /api-interface        # Api Gateway Service
    |- /board-service        # Board Service 
    |- /card-service         # Card Service 
    |- /list-service         # List Service
    |- /profile-service      # Profile Service 
    |- service-registry      # Service Registry
    |- workspace-registry    # Workspace Service 
```

Explore **_Readme file_ of each service** to know in detail.

---
### Run it on your machine

There are **7 services** of this application, Go through Readme file of each to run that service.

---