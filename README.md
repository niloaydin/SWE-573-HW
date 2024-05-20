# Community Application

This is a community-specific information application developed as part of the Bogazici University 2024/Spring SWE 573 course project.

## Overview

The Community Information Application is designed to provide a platform where users can create and join communities, share information through posts, and engage with other users through commenting. The application is built using Java Spring Boot for the Backend and React for the Frontend.

Frontend repository can be reached through [this link](https://github.com/niloaydin/SWE-573-FE). However, it's important to keep in mind that, this repository is the main repository for the project and all the issues have been created both for frontend and backend in this repository. Each frontend change has been referenced with commit link in the issues.

Deployed project can be reached through [this link](http://16.170.162.125:3000/).

## Features

- **User Authentication**: Users can create accounts and log in securely.
- **Community Creation and Joining**: Users can create new communities or join existing ones.
- **Post Creation**: Users can create posts within communities, sharing information and initiating discussions.
- **Post Templates**: Community-specific post templates allow for structured information sharing.


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

#### Run the Docker Container
To run the Docker container, use the following command:
```bash
docker compose up -d
```


