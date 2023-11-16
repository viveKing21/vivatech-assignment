# Spring Boot MVC Application with OTP Authentication

This project is a Java Spring Boot MVC application developed as part of a job assignment. The application provides REST APIs for user authentication using OTP (One-Time Password). It includes functionalities such as login, register, sending OTP, verifying OTP, and fetching user profiles.

## Backend - Spring Boot

### API Routes

- **Get User Profile:**
  - Endpoint: `http://localhost:8000/user` [GET]

- **Verify OTP:**
  - Endpoint: `http://localhost:8000/user/verify` [GET]

- **Send OTP:**
  - Endpoint: `http://localhost:8000/user/send-otp` [GET]

- **User Login:**
  - Endpoint: `http://localhost:8000/user/login` [POST]

- **User Registration:**
  - Endpoint: `http://localhost:8000/user/register` [POST]

## Frontend

- **Home:**
  - Path: `/`

- **Login:**
  - Path: `/login`

- **Register:**
  - Path: `/register`

## Problem Statement

### Develop Rest API

1. **OTP API Send:**
   - Endpoint: `/api/send-otp` [POST]

2. **OTP Validates API:**
   - Endpoint: `/api/validate-otp` [POST]

3. **User Profile API:**
   - Endpoint: `/api/user-profile` [GET]

### JWT Token for Session and Authorization Management

JWT (JSON Web Token) is used for session management and API authorization.

## HTML Page for Testing APIs

An HTML page is included in the project to test the mentioned APIs.

## Technologies Used

- Java Spring Boot
- Database: [Your Database Choice]

## Project Structure

The project follows the standard Spring Boot MVC structure.

## Running the Application

1. Clone the repository: `git clone [repository-link]`
2. Navigate to the project directory: `cd [project-directory]`
3. Run the application: `./mvnw spring-boot:run`

## Postman Collection

A Postman collection containing the APIs for easy testing is included. You can find it in the `postman` directory.

## Solution Document

For detailed information about the application and its components, refer to the [Solution Document](link-to-solution-document).

Feel free to reach out if you have any questions or issues.

## Configuration

- Backend
  
Update applicaion.properties file with your sql and smtp.mail details

- Frontend

Update utils/Axios.js base url of the API
