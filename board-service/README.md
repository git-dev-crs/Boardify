## Board Service - Boardify

### Overview
The **Board Service** is a core part of the Boardify system that handles everything related to task boards. It provides APIs to _create, retrieve, manage, and delete boards_, including handling user membership and tags within boards. It supports both standalone board creation and creation via workspaces.

---

### Responsibilities
- Create & manage boards and their details.
- Create a board or create via workspaces
- Handle user invitations and access control for boards.
- Add tags to be used in board.

---

### Folder Structure
```text
board-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.boardify.board_service/
│   │   │       ├── controller/
│   │   │       │   └── BoardController.java            # Contains REST endpoints for board operations
│   │   │       ├── dto/
│   │   │       │   ├── request/                        # DTOs used for incoming API requests
│   │   │       │   ├── response/                       # DTOs used for sending responses to clients
│   │   │       │   ├── service/                        # DTOs used for inter-service communication
│   │   │       │   └── ResponseWrapper.java            # Wrapper for uniform success & error responses
│   │   │       ├── feign/
│   │   │       │   ├── ListInterface.java              # Feign client for List Service
│   │   │       │   ├── WorkspaceInterface.java         # Feign client for Workspace Service
│   │   │       │   ├── ProfileInterface.java           # Feign client for Profile Service
│   │   │       │   ├── FeignClientConfig.java          # Custom Feign configuration (e.g., interceptors)
│   │   │       │   └── CustomDecoder.java              # Decoder for handling DateTime & ResponseWrapper
│   │   │       ├── model/                              # JPA entities mapped to MySQL tables
│   │   │       ├── repository/                         # Spring Data JPA repositories
│   │   │       ├── service/
│   │   │       │   └── BoardService.java             # Contains core business logic for boards
│   │   │       └── BoardServiceApplication.java      # Main Spring Boot application entry point
│   └── resources/
│       └── application.properties                      # Spring Boot configuration file
├── test/                                               # Unit and integration tests
├── pom.xml                                             # Maven build configuration
└── README.md                                           # Project documentation
```

---

### Endpoints to use
```text
    GET - /board/details/{board_id}?userId={userId}               # Get board details for given user
    GET - /board/{board_id}?userId={userId}                       # Get board with lists and cards

    POST - /board/create                                         # Create a standalone board
    POST - /board/createFromWorkspace                            # Create a board from workspace context
    POST - /board/{board_id}/invite                               # Invite a user to a board
    POST - /board/{board_id}/addTag                               # Add a tag to the board
    POST - /board/removeUser                                     # Remove a user from a board

    DELETE - /board/deleteBoard/{boardId}?userId={userId}         # Delete a board (only by authorized user)
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

3. Port Number: _8082_
4. Application Name: _board-service_

---