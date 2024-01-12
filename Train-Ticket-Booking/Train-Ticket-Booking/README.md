# Ticket-Booking-Application

-Start: The application begins with TicketApplication.java, bootstrapping the Spring Boot framework.

-Web Layer: HTTP requests are handled by TicketController.java and UserController.java. 
 These controllers receive requests from clients (like Swagger, Postman), process them, and return responses. 
 They use TicketDTO.java and UserDTO.java to transfer data.

-Service Layer: The controllers delegate business logic to the service layer, consisting of TicketService.java, TicketServiceImpl.java, UserService.java, and UserServiceImpl.java.
 This layer is responsible for operations like CRUD actions and business rules.

-Data Access Layer: The service layer interacts with the database through JPA repositories (TicketRepo.java and UserRepo.java).
 These repositories manage entity persistence for Ticket.java and User.java.

-Exception Handling: APIException.java, GlobalExceptionHandler.java, and ResourceNotFoundException.java are used for handling and returning exceptions in a user-friendly format.

-Configuration and Utilities: MyConfig.java provides essential configurations, like setting up ModelMapper. Property files (application.properties) configure database connections and application settings.

# Technologies:
- Java 17 
- Spring Boot 3.2.1
- Maven
- MySQL
- Spring Data JPA
- Swagger UI

# Running the app
1.Import the project into Intellij
- Click File>Open
- navigating to the project's root directory. Select the project folder and click ok.
                    OR
Import the project into STS:
  - Click File > Import...
  - Select Maven > Existing Maven Projects and click Next
  - Browse to the project directory and click Finish
                
2.Update the values in application.properties with your MySQL database connection details.
3.Run the app: Find the main class of your Spring Boot application. This is typically a class annotated with @SpringBootApplication. It often contains the main method.
-Run the Application:
Right-click on the main class file.
Select Run <Your Main Class> from the context menu.
Alternatively, you can use the green play button located in the top toolbar.

# API documentation
- API documentation is available via Swagger UI at http://localhost:8080/swagger-ui/index.html

# Thank You
