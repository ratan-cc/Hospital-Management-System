# Hospital Management System

A comprehensive web-based Hospital Management System built with Java, Spring Boot, Thymeleaf, and MySQL. The system allows management of patients, doctors, departments, appointments, and insurance policies through a modern, user-friendly interface.

## Features

- **Patient Management:** Add, edit, list, and delete patients. Each patient has details like name, email, birth date, gender, blood group, insurance, and appointment history.
- **Doctor Management:** Manage doctors, their specializations, emails, and department associations.
- **Department Management:** Create and manage hospital departments, assign head doctors, and associate doctors with departments.
- **Appointment Scheduling:** Schedule, edit, and list appointments between patients and doctors, with reasons and timestamps.
- **Insurance Management:** Manage insurance policies, providers, and validity for patients.
- **Error Handling:** User-friendly error pages for missing resources and generic errors.
- **Modern UI:** Responsive and visually appealing interface using custom CSS and Thymeleaf templates.

## Tech Stack

- **Backend:** Java 21, Spring Boot 3.5.3
  - Spring Web (RESTful APIs, MVC)
  - Spring Data JPA (ORM, database access)
  - Spring Boot Starter Test (testing)
- **Frontend:** Thymeleaf (server-side rendering), HTML5, CSS3
- **Database:** MySQL (configured in `application.properties`)
- **ORM:** Hibernate (via JPA)
- **Build Tool:** Maven
- **Lombok:** For reducing boilerplate code in entities and services

## Project Structure

```
src/
  main/
    java/in/main/hospitalManagement/
      controller/   # Spring MVC controllers for each module
      entity/       # JPA entities: Patient, Doctor, Department, Appointment, Insurance
      exception/    # Custom exceptions and global handler
      repository/   # Spring Data JPA repositories
      service/      # Service interfaces
      serviceimpl/  # Service implementations
    resources/
      application.properties  # DB and Spring config
      static/
        css/styles.css        # Custom styles
        Js/index.js           # (empty, for future JS)
      templates/
        index.html            # Dashboard
        error.html            # Error page
        [module]/form.html    # CRUD forms
        [module]/list.html    # List views
  test/
    java/                     # Unit and integration tests
pom.xml                       # Maven dependencies and plugins
```

## How It Works

- **Spring Boot** initializes the application and auto-configures components.
- **Controllers** handle HTTP requests and map them to service methods.
- **Services** encapsulate business logic and interact with repositories.
- **Repositories** provide CRUD operations for entities using JPA.
- **Thymeleaf templates** render dynamic HTML pages for each module.
- **Custom error handling** ensures graceful user experience.

## Configuration

- Database connection and JPA settings are in `src/main/resources/application.properties`.
- Example:
  ```
  spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db
  spring.datasource.username=root
  spring.datasource.password=your_password
  spring.jpa.hibernate.ddl-auto=update
  ```

## Running the Project

1. **Clone the repository**
2. **Configure MySQL** and update credentials in `application.properties`
3. **Build and run:**
   ```
   ./mvnw spring-boot:run
   ```
4. **Access the app:**  
   Open [http://localhost:8080](http://localhost:8080) in your browser.

## Dependencies

- `spring-boot-starter-data-jpa`
- `spring-boot-starter-web`
- `spring-boot-starter-thymeleaf`
- `mysql-connector-j`
- `lombok`
- `spring-boot-starter-test`

## Screenshots

- Dashboard, patient/doctor/department/appointment/insurance management, and error pages (see `/src/main/resources/templates/`).

## License

This project is for educational purposes.
