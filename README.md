# Clinker
Clinker is a franchise management service backed by a REST API built with Java and Spring Boot. 

Features:
  - manage franchisor and franchisee data
  - invoice and payment system
  - generate and track franchise fees (ex. royalties and marketing fees based on total sales)
  - record sales and expenses and generate financial metrics

## Setup Locally
### Requirements
- Java 9 or later
- Maven
### Installation
```shell
 git clone https://github.com/xiao-vincent/clinker.git
 cd clinker 
```
#### Database Configuration
This application currently uses PostgreSQL. To use a different database,
change the settings defined in the `application.properties` file.
For more information, checkout [Spring's documentation on how to work with
databases](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html).

### Run the server on `localhost:8080` with
```shell
./mvnw spring-boot:run
```
or use your IDE's build/run tool

### Test Connection
Test the api with
```shell 
curl -i localhost:8080/api/ping
```
and you should get response
```
HTTP/1.1 200 OK
```


## Built With
* [Spring](https://spring.io//) - Web Framework 
    - Spring Boot - preconfiguration
    - Spring Data JPA - JPA data access abstraction 
    - Spring Security - authentication and access-control framework
* [PostgreSQL](https://www.postgresql.org/) - Relational Database
* [Maven](https://maven.apache.org/) - Dependency Management


