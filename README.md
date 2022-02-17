# LoanManagementSystem-BE
Loan Management Backend Microservice

### Technologies Used
- Java Spring Boot
- Spring Data JPA
- MySQL Database

### System Design
- For user-based authentication, we are using a simple JWT token-based implementation. This is implemented using the Spring Security project.
- Spring Data JPA is being used for JPA based data access layers. DB entities, their relationships and repositories are being exposed using JPA.
- For auto settlement of the loans, Spring Schedulers are being used along with Spring @Async to execute the settlement request in parallel based on configured thresholds by the scheduler.

### Database Schema 
![image](https://user-images.githubusercontent.com/12005138/154504507-8fccc00d-89c3-4bd7-9d5a-80d012f5ed25.png)

### Improvements
- Shedlock/Quartz job-scheduling framework to make sure our scheduled tasks run only once at the same time in a multi-instance environment
- Application Dockerization
- Complete Microservices architecture using Spring Cloud/Kubernetes
