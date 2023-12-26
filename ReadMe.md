# CTPL Interview Application

## 1. What is CTPL Interview Application

The CTPL Interview Application is a project designed for conducting interviews. It provides a platform for managing user registrations, authentication, and conducting interview-related tasks.

## 2. Project Setup on Local

### 2.1 Necessary Software Installations

To set up the CTPL Interview Application locally, make sure you have the following software installed:

- Java Development Kit (JDK)
- MySQL Database (or any preferred relational database)
- IDE (e.g., IntelliJ IDEA, Eclipse)

### 2.2 Code setup

1. Clone the repository:

   ```bash
   git clone https://github.com/shreyashHake/connecticus-quiz-app.git
   cd connecticus-quiz-app
   ```

### Backend Setup

1. Navigate to the backend directory or open in preferred IDE:

   ```bash
   cd connecticus-quiz-application-server

   ```

2. Configure your MySql database settings in application.properties:

   ```bash
   # Database Configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/questionsDb
   spring.datasource.username=your_username
   spring.datasource.password=your_password

   # JPA Configuration
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   spring.jpa.hibernate.ddl-auto=update

   # Cors
   Access-Control-Allow-Origin= http://localhost:3000
   Access-Control-Allow-Methods= GET, POST, PUT, DELETE
   Access-Control-Allow-Headers= Content-Type, Authorization
   Access-Control-Allow-Credentials= true

   ```

3. Create a database in MySql workbenc of name `questionsDb`.

## 3. Boot/Build Application Commands

### 3.1 Building the Application

Build and run the Spring Boot backend using IDE.
