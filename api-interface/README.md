## API Interface - Boardify

This is the **API Interface** (Gateway) service of the **Boardify** project. It acts as a centralized entry point for all client requests, routing them to appropriate backend microservices. 

---

### Responsibilities

- Central API gateway for routing requests to microservices.
- Service discovery via Eureka to dynamically resolve service locations.
- CORS and global error handling.
- Potential future support for rate limiting and authentication filters.

---

### Folder Structure

```text
api-interface/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.boardify.api_interface/
│   │   │       └── ApiInterfaceApplication.java      # Main Spring Boot application entry point
│   └── resources/
│       └── application.yml                           # Spring Boot configuration file
├── test/                                               # Unit and integration tests
├── pom.xml                                             # Maven build configuration
└── README.md                                           # Project documentation
```

---

### How to run on your machine

1. Port Number: _8088_
2. Application Name: _api-interface_

---
