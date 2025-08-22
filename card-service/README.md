## Card Service - Boardify
### Overview
The **Card Service** handles all operations related to cards within lists on the Boardify platform. It supports _creating, retrieving, moving, joining, commenting, tagging, and managing checklists on cards_. The service also supports _users joining and leaving cards_ and provides _secure deletion of cards and comments_.

---
### Responsibilities

- Retrieve detailed card information by card ID and user context.
- Create new cards within lists.
- Move cards between lists or positions.
- Manage card memberships (join and leave cards).
- Add and delete comments on cards.
- Manage card checklists (marking items done).
- Add tags to cards for categorization.
- Delete cards and comments with proper user authorization.

---

### Folder Structure
```text
card-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.boardify.card_service/
│   │   │       ├── controller/
│   │   │       │   └── CardController.java            # Contains REST endpoints for card operations
│   │   │       ├── dto/
│   │   │       │   ├── request/                        # DTOs used for incoming API requests
│   │   │       │   ├── response/                       # DTOs used for sending responses to clients
│   │   │       │   ├── service/                        # DTOs used for inter-service communication
│   │   │       │   └── ResponseWrapper.java            # Wrapper for uniform success & error responses
│   │   │       ├── feign/
│   │   │       │   ├── BoardInterface.java              # Feign client for Board Service
│   │   │       │   ├── ListInterface.java               # Feign client for List Service
│   │   │       │   ├── ProfileInterface.java           # Feign client for Profile Service
│   │   │       │   ├── FeignClientConfig.java          # Custom Feign configuration 
│   │   │       │   └── CustomDecoder.java              # Decoder for handling DateTime & ResponseWrapper
│   │   │       ├── model/                              # JPA entities mapped to MySQL tables
│   │   │       ├── repository/                         # Spring Data JPA repositories
│   │   │       ├── service/
│   │   │       │   └── ListService.java             # Contains core business logic for cards
│   │   │       └── CardServiceApplication.java      # Main Spring Boot application entry point
│   └── resources/
│       └── application.properties                      # Spring Boot configuration file
├── test/                                               # Unit and integration tests
├── pom.xml                                             # Maven build configuration
└── README.md                                           # Project documentation
```

---

### Endpoints to use
```text
GET    - /card/details/{cardId}?userId={userId}          # Get detailed card info for a user

POST   - /card/create                                    # Create a new card
POST   - /card/moveCard                                  # Move a card to a different list or position
POST   - /card/joinCard                                  # User joins a card
POST   - /card/addComment                                # Add a comment to a card
POST   - /card/markChecklist                             # Mark an item in the checklist on a card
POST   - /card/addTag                                    # Add a tag to a card
POST   - /card/leaveCard                                 # User leaves a card

DELETE - /card/deleteCard/{cardId}?userId={userId}       # Delete a card (with user validation)
DELETE - /card/deleteComment/{commentId}?userId={userId} # Delete a comment on a card (with user validation)

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

3. Port Number: _8085_
4. Application Name: _card-service_
---

