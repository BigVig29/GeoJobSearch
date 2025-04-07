# GeoJobSearch

This project will serve as a guide on how to setup a three part application that includes a Spring server, a SQL database and a React frontend both locally and on a Kubernetes host.

## Local Setup

To setup the project locally, you must have [docker](https://www.docker.com/products/docker-desktop/) installed and running on your machine. Once it is setup, run the following command:

```
docker compose up
```

This will setup the app and you can access the following addresses on your browser:
- React frontend = http://localhost:3000
- Spring backend = http://localhost:8080/api (add **/docs.html** to access Swagger UI)

## Description

Job search web application using Beautiful Soup, Selenium, and Spring Boot to
scrape, aggregate, and display job listings from Indeed.com, integrating a Python backend
with a MariaDB database for efficient data management.
Designed and implemented a React frontend with advanced filtering, sorting, and search
functionalities, enabling users to refine job searches by salary, location, and job type, while
ensuring seamless pagination and display.
Led backend development, including RESTful API creation, job preprocessing, and database
schema maintenance, while conducting unit/integration testing and peer code reviews to
ensure high code quality and system reliability.

