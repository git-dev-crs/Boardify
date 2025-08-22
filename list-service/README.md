## List Service - Boardify
### Overview
The **List Service** manages the lists within boards in the Boardify system. It provides APIs to _create, retrieve, and delete lists, as well as fetch lists along with their associated cards_.

---

### Responsibilities
- Retrieve list details by list ID with user context.
- Retrieve lists including their cards for detailed views.
- Create new lists within boards.
- Delete individual lists or all lists from a specific board.
- Validate user membership related to lists and boards for access control.

---

### Folder Structure
```text
list-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.boardify.list_service/
│   │   │       ├── controller/
│   │   │       │   └── ListController.java            # Contains REST endpoints for list operations
│   │   │       ├── dto/
│   │   │       │   ├── request/                        # DTOs used for incoming API requests
│   │   │       │   ├── response/                       # DTOs used for sending responses to clients
│   │   │       │   ├── service/                        # DTOs used for inter-service communication
│   │   │       │   └── ResponseWrapper.java            # Wrapper for uniform success & error responses
│   │   │       ├── feign/
│   │   │       │   ├── BoardInterface.java              # Feign client for Board Service
│   │   │       │   ├── CardInterface.java               # Feign client for Card Service
│   │   │       │   ├── ProfileInterface.java           # Feign client for Profile Service
│   │   │       │   ├── FeignClientConfig.java          # Custom Feign configuration 
│   │   │       │   └── CustomDecoder.java              # Decoder for handling DateTime & ResponseWrapper
│   │   │       ├── model/                              # JPA entities mapped to MySQL tables
│   │   │       ├── repository/                         # Spring Data JPA repositories
│   │   │       ├── service/
│   │   │       │   └── ListService.java             # Contains core business logic for lists
│   │   │       └── ListServiceApplication.java      # Main Spring Boot application entry point
│   └── resources/
│       └── application.properties                      # Spring Boot configuration file
├── test/                                               # Unit and integration tests
├── pom.xml                                             # Maven build configuration
└── README.md                                           # Project documentation
```

---

### Endpoints to use
```text
GET    - /list/details/{list_id}            # Get list details 
GET    - /list/{list_id}?userId={userId}    # Get a list with its cards

POST   - /list/create                       # Create a new list (body: CreateListDto)

DELETE - /list/deleteList/{list_id}?userId={userId}    # Delete a specific list (with user validation)

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

3. Port Number: _8084_
4. Application Name: _list-service_

---

