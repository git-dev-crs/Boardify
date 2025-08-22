## Profile Service - Boardify

### Overview

The **Profile Service** is a part of the Boardify project that manages user profile data. It interacts with the other service to provide data for a user like workspaces, board, invitations of a user.

---

### Responsibilities

- Store and manage extended user profile data.
- CRUD operations on user & related data.
- Integrate with other services (e.g., Board, Workspace) for provide data associated to a user.

---

### Folder Structure
```text
profile-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.boardify.profile_service/
│   │   │       ├── controller/
│   │   │       │   └── ProfileController.java         # Contains REST endpoints for profile operations
│   │   │       ├── dto/
│   │   │       │   ├── request/                        # DTOs used for incoming API requests
│   │   │       │   ├── response/                       # DTOs used for sending responses to clients
│   │   │       │   ├── service/                        # DTOs used for inter-service communication
│   │   │       │   └── ResponseWrapper.java            # Wrapper for uniform success & error responses
│   │   │       ├── feign/
│   │   │       │   ├── BoardInterface.java             # Feign client for Board Service
│   │   │       │   ├── WorkspaceInterface.java         # Feign client for Workspace Service
│   │   │       │   ├── FeignClientConfig.java          # Custom Feign configuration (e.g., interceptors)
│   │   │       │   └── CustomDecoder.java              # Decoder for handling DateTime & ResponseWrapper
│   │   │       ├── model/                              # JPA entities mapped to MySQL tables
│   │   │       ├── repository/                         # Spring Data JPA repositories
│   │   │       ├── service/
│   │   │       │   └── ProfileService.java             # Contains core business logic for profiles
│   │   │       ├── utills/                             # Utility/helper classes 
│   │   │       └── ProfileServiceApplication.java      # Main Spring Boot application entry point
│   └── resources/
│       └── application.properties                      # Spring Boot configuration file
├── test/                                               # Unit and integration tests
├── pom.xml                                             # Maven build configuration
└── README.md                                           # Project documentation

```

---

### Endpoints to use
```text
    GET - /profile/details/{userId}                   # Returns User Details for userId
    GET - /profile/home/{userId}                      # Return Workspaces & Boards user id part of
    GET - /profile/workspaceInvitations/{userId}      # Return invitations of workspaces user has got
    GET - /profile/boardInvitations/{userId}          # Return invitations of boards user has got
    GET - /profile/workspaces/{userId}                # Return Workspaces user id part of
    GET - /profile/boards/{userId}                    # Return Boards user id part of
    
    POST - /profile/createAccount                   # Creates new user account
    POST - /profile/decideWorkspaceInvitation       # Decide invitation of workspace that user has got
    POST - /profile/decideBoardInvitation           # Decide invitation of board that user has got
    
    DELETE - /profile/deleteAccount/{userId}          # Delete user account
```

### How to run on your machine

To run this service locally, you need to configure your MySQL database credentials.

1. Open the `env.properties` file (create it if it doesn’t exist).
2. Add the following entries with your MySQL database information:

```properties
# MySQL Database Configuration
DB_USER=<mysql_user>
DB_NAME=<mysql_name>
DB_PASSWORD=<mysql_password>
```

3. Port Number: _8080_
4. Application Name: _profile-service_

---