## Workspace Service - Boardify
### Overview
The **Workspace Service** is a part of the Boardify project responsible for managing workspace-related functionality. It handles operations such as _workspace creation, user invitations, membership management, and fetching workspace details along with associated boards_.

---

### Responsibilities
- Create and manage workspaces.
- Handle user invitations to workspaces.
- Retrieve workspace details and board information for a given user.
- Integrate with other services (e.g., Profile, Board) for complete data management.

---

### Folder Structure
```text
workspace-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.boardify.workspace_service/
│   │   │       ├── controller/
│   │   │       │   └── WorkspaceController.java         # Contains REST endpoints for workspace operations
│   │   │       ├── dto/
│   │   │       │   ├── request/                        # DTOs used for incoming API requests
│   │   │       │   ├── response/                       # DTOs used for sending responses to clients
│   │   │       │   ├── service/                        # DTOs used for inter-service communication
│   │   │       │   └── ResponseWrapper.java            # Wrapper for uniform success & error responses
│   │   │       ├── feign/
│   │   │       │   ├── BoardInterface.java             # Feign client for Board Service
│   │   │       │   ├── ProfileInterface.java           # Feign client for Profile Service
│   │   │       │   ├── FeignClientConfig.java          # Custom Feign configuration (e.g., interceptors)
│   │   │       │   └── CustomDecoder.java              # Decoder for handling DateTime & ResponseWrapper
│   │   │       ├── model/                              # JPA entities mapped to MySQL tables
│   │   │       ├── repository/                         # Spring Data JPA repositories
│   │   │       ├── service/
│   │   │       │   └── WorkspaceService.java             # Contains core business logic for workspaces
│   │   │       └── WorkspaceServiceApplication.java      # Main Spring Boot application entry point
│   └── resources/
│       └── application.properties                      # Spring Boot configuration file
├── test/                                               # Unit and integration tests
├── pom.xml                                             # Maven build configuration
└── README.md                                           # Project documentation
```

---

### Endpoints to use
```text
    GET - /workspace/details/{workspaceId}?userId={userId}      # Get workspace details for given user
    GET - /workspace/{workspaceId}?userId={userId}              # Get workspace with its boards for given user
    
    POST - /workspace/create                                    # Create a new workspace
    POST - /workspace/{workspaceId}/invite                      # Invite a user to the workspace
    POST - /workspace/removeUser                                # Remove a user from a workspace
    
    DELETE - /workspace/deleteWorkspace/{workspaceId}?userId={userId}   # Delete a workspace (only by authorized user)
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

3. Port Number: _8081_
4. Application Name: _workspace-service_

---