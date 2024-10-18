# Sistema de Autenticación con JWT en Spring Boot

This repository is a basic template implementing an authentication system using JWT (JSON Web Token) in Spring Boot. The template is designed to be easily extendable and adaptable to any project that requires authentication and authorization using JWT.

## Characteristics

- JWT Authentication: Implements the generation and validation of JWT tokens.
- Registration and Login: Endpoints for user registration and login.
- Route Protection: Routes protected by authorization filters that validate the JWT.
- Role and Permission Management: Example roles that restrict access to certain routes.
- Seguridad Spring Security: Configurado para trabajar de manera eficiente con JWT.

## Emplyed Technologies

- Spring Boot: Framework for building Java applications.
- Spring Security: Security module for managing authentication and authorization.
- JWT (JSON Web Tokens): Token-based authentication mechanism.
- H2 Database: In-memory database for testing (can be replaced with another DB engine for production).
- Maven: For dependency management.

## Requirements

- Java 17+
- Maven

## Installation

1. Clone the repository

```bash
    git clone https://github.com/miguelanguai/jwt-spring-auth-template.git
    cd jwt-spring-auth-template
```

2. Configure the database

In the file `src/main/resources/application.properties`, you can configure the database of your choice. By default, the project uses H2, an in-memory database for development and testing.

3. Install dependencies and build the project

```bash
    mvn clean install
```

4. Run the application

```bash
    mvn spring-boot:run
```

## Usage

### Registration

- Enpoint: `POST /register`
- Description: Create a new user
- Request Body:

```JSON
{
    "firstName":"John",
    "lastName":"Doe",
    "username":"johndoe",
    "mail":"johndoe@mail.com",
    "password":"helloworld",
    "role":"USER"
}
```

### Login

- Enpoint: `POST /login`
- Description: Start a session
- Request Body:

```JSON
{
    "username":"johndoe",
    "password":"helloworld"
}
```

- Successful response:

```JSON
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5c..."
}
```

### Logout

- Enpoint: `POST /user/signout`
- Description: Terminate session
- Headers: `Authorization: Bearer <JWT_TOKEN>`
- Request Body:

```JSON
{
    "username":"johndoe",
    "password":"helloworld"
}
```

### Get Entities

- Enpoint: `GET /p/employee`
- Description: Get all the employees

### Create Entity

- Enpoint: `PUT /u/employee`
- Description: Create a new employee
- Headers: `Authorization: Bearer <JWT_TOKEN>`
- Request Body:

```JSON
{
    "name":"Sarah";
}
```

### Edit Employee

- Enpoint: `POST /register`
- Description: Edit an existing user
- Headers: `Authorization: Bearer <JWT_TOKEN>`
- Request Body:

```JSON
{
    "name":"new name";
}
```

## Classes and their use

### Authentication folder

#### Role

Enum.

It is ADMIN (more privileges) or USE(less).

#### Token

Entity.

It has an id, a token, a boolean loggedOut and a user.

#### Authentication Controller

Endpoints for authentication: Register, login and signout.

#### AuthenticationResponse

Entity of the Authentication Response.

#### AuthenticationService

It has the methods that manage the logic for registring, authenticating and logging out the user.

It also revokes all tokens by user and creates a new token to assign to the user.

#### JwtAuthenticationFilter

It manages the logic with the token. It validates if token is valid.

#### JwtService

It extractUsername from Token.

It checks if a token is valid.

It checks if a token is expired.

It extracts the expiration date out of the token.

It extracts the claims out of the token.

It generates a new token.

It gets the signing key out of a secret key.

#### TokenRepository

#### UserDetailsServiceImpl

It returns a User entity.

### Config folder

#### CustomLogoutHandler

It handles the logout process by marking a token as logged out.

#### ModelMapperConfig

Nothing related to Authentication process.

It helps to deliver lists on the endpoint.

#### SecurityConfig

This class handles the security filter chain. It gives the authorization to the calls thrown.

It configures the user details service.

It configures the session creation policy.

It configures the personalized filters.

It configures the exceptions thrown when authentication is not correct.

It also configures how logout is managed.

There is another method to encode and decode the hashed passwords.

### Employee folder

folder to show how a CRUD application is with authentication.

### User folder

folder where CRUD of users happens.

## Customization

- Routes: You can add more protected or public routes as needed in the controller.
- Roles: Modify the role system in the User class and in the controllers to enforce more specific permissions.

## Contributions

Contributions are welcome. If you want to improve or extend this template's functionality, feel free to create a Pull Request or open an Issue.

## License

This project is licensed under the MIT License. You can find more details in the LICENSE file.
