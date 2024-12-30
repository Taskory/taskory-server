**Taskory Server**

Taskory is a schedule and task management web application designed to emphasize simplicity and productivity. This repository contains the backend server codebase for the Taskory project.



**📚 Project Overview**

The Taskory Server provides RESTful APIs and supports the following key features:

- Management of tasks and schedules

- Tag and status management

- User authentication and authorization using OAuth2 and JWT

- Validation and error handling



**🛠️ Tech Stack**

- **Language:** Java 17

- **Framework:** Spring Boot 3.3.1

- **Database:** MariaDB

- **Build Tool:** Gradle

- **API Design:** RESTful

- **Others:** JPA, Hibernate, OAuth2, JWT, Lombok



**🛠️ Dependencies**

The project uses the following key dependencies:

- **Spring Boot**: Starter libraries for Web, JPA, Security, OAuth2 Client, and Validation.

- **Lombok**: Simplifies Java development by reducing boilerplate code.

- **MariaDB**: Runtime dependency for database connectivity.

- **JUnit 5**: For testing.

- **JWT (Java JSON Web Token)**: For authentication and token management.

Refer to the build.gradle file for the complete list of dependencies.



**🚀 Getting Started**

**1. Prerequisites**

**Requirements:**

- Java 17 or higher
- Gradle 7.x or higher
- MariaDB installed



**Database Configuration**

Set your database connection details in the application.yml file.

```yaml
spring:
 datasource:
  url: jdbc:mariadb://localhost:3306/taskory
  username: your_username
  password: your_password
 jpa:
  hibernate:
   ddl-auto: update
```



**2. Running the Server**

1. Build the project using Gradle:

```cmd
./gradlew build
```

2. Run the server:

```
./gradlew bootRun
```



**3. API Testing**

Once the server is running, the default base URL is http://localhost:8080. You can explore the API documentation via [Swagger](http://localhost:8080/swagger-ui/).



**🧪 Testing**

Run the tests using the following command:

```cmd
./gradlew test
```

**Notes:**

- JUnit 5 is used for unit and integration testing.
- The project is configured to use the JUnit platform launcher for enhanced testing capabilities.



**🤝 Contributing**

1. Create an issue to describe your feature or bug fix.
2. Create a new branch (feat/your-feature-name).
3. Commit your changes (git commit -m 'Add your message here').
4. Push the branch (git push origin your-branch-name).
5. Open a Pull Request.



**📄 License**

Taskory Server is licensed under the MIT License. See the LICENSE file for details.



**📞 Contact**

- Email: ysw991106@gmail.com