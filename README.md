# Community Application

This is a community-specific information application developed as part of the Bogazici University 2024/Spring SWE 573 course project.

## Overview

The Community Information Application is designed to provide a platform where users can create and join communities, share information through posts, and engage with other users through commenting. The application is built using Java Spring Boot for the backend.

## Features

- **User Authentication**: Users can create accounts and log in securely.
- **Community Creation and Joining**: Users can create new communities or join existing ones.
- **Post Creation**: Users can create posts within communities, sharing information and initiating discussions.
- **Post Templates**: Community-specific post templates allow for structured information sharing.
- **Commenting** (To be implemented): Users will be able to comment on posts to engage in discussions.
- **Voting** (To be implemented): Users will be able to vote on posts and comments to express their opinions.

## Technologies Used

- **Backend**:
  - Java Spring Boot
  - PostgreSQL
  - Hibernate for ORM
- **Authentication**:
  - JSON Web Tokens (JWT) for secure authentication

## Database Connection

To connect to the database, users need to provide their database credentials in the `application.properties` file located in the `src/main/resources` directory of the backend module.

Here's an example of how to configure the database connection:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/community_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Replace `your_username` and `your_password` with your PostgreSQL username and password.


## Getting Started

To run the application locally, follow these steps:

1. Clone this repository to your local machine.
2. Navigate to the `backend` directory.
3. Run `./mvnw spring-boot:run` to start the backend server.
4. Open your browser and visit [http://localhost:8080](http://localhost:8080) to view the application.

### Run the Application Using Docker
#### Prerequisities
You need to have docker installed in order to run the docker file.
#### Build Docker Image

To build the Docker image, use the following command:

```bash
docker build -t community-application .
```
#### Run the Docker Container
To run the Docker container, use the following command:
```bash
docker run -p 8080:8080 community-application
```


