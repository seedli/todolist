# Tom's Todolist api demo
This is my api demo work to demostrate the manipulation of the todo lists.

# prerequisite
**Jdk11** and **Maven**, **Docker** are required.
# steps to run up this application
Please follow the following steps to run up this application.
## 1. create a MySql instance
This application is running based on a MySql database, please start a instance by using Docker at first.
```bash
docker network create demo-network
docker run --name mysql --network demo-network -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=demoDB -d mysql:8
```
I created a new network as **demo-network** in order to let the followed up application instance can be involved inside the same network to make them can be communicated each other.
## 2. clone and build this repository
Clone this repository and switch to the root directory of it, then use the maven to build this application up.
```bash
mvn clean install
```
or build it without running test
```bash
mvn clean install -DskipTests
```
Once the maven build is succeeded, run the docker command to build a image and start up a instance.
```
docker build -t demo-app .
docker run --name demoApp --network demo-network -p 8080:8080 -d demo-app
```
Of course, you can modify the port **-p [port]:8080** as your desired.
## 3. affirm the application is running up
Open the swagger ui link to affirm the application has been started up.

http://localhost:8080/todolist/swagger-ui.html

# Funtionalities
## User controlling
Once the application has been started, there are three user accounts with different role have been pre-populated
- admin (role : ADMIN)
- tom (role : USER)
- jerry (role : USER)

Their default password all are **Truck123**

Users with ADMIN role can do any manipulation of the funtions of this application.
Normal USER can view the user list, chenge their password, view the lists which are belong to them or shared with them, and update their items.

### API Examples
#### Get user list
```http
GET /todolist/user/list
```
#### Create a user
Note: only ADMIN can create user
```http
POST /todolist/user
```
```json
{
     "name": "wendy",
     "password" : "123",
     "roleId": 1
}
```
#### Update password
Note: USER can only modify their password
```http
PUT /todolist/user/password
```
```json
{
    "id": 2,
    "password" : "pwd123"
}
```
## List controlling
As a USER I can investigate my own lists and do any manipulation of them.
### API Examples
#### Get my list
```http
GET /todolist/list/my
```
#### Update list
```http
PUT /todolist/list
```
```json
{
    "id": 2,
    "password" : "pwd123"
}
```
#### Create a new list
```http
POST /todolist/list
```
```json
{
    "name": "demo list",
    "userId": 1
}
```
#### Share my list with other user
```http
PUT /todolist/list/share/{listId}/{useId}
```
#### Get lists shared with me
```http
GET /todolist/list/sharedWithMe
```
#### Revoke the shared list from other user
```http
PUT /todolist/list/revoke/{listId}/{useId}
```
#### Get list items
Of course, you can investigate the items within the list
```http
GET /todolist/list/{listId}?orderBy={orderBy}&sort={sort}&status={status}
```
There are 3 optional params for listing the items you desired to investigate
- orderBy: you can sort item by **sortOrder** or **deadline**
- sort: **asc** or **desc**
- status: use **expired** or **unexpired** to filter out the items wheater are exceeded the deadline or not, or showing entire list if not specified.
## Item Controlling
As a user, I can manage the items within my lists.
### API Examples
#### Create an item
```http
POST /todolist/item
```
```json
{
    "name": "go home",
    "description": "time to go home",
    "listId": 1,
    "done": false
}
```
#### Update an item
```http
PUT /todolist/item
```
```json
{
    "id": 8,
    "name": "go home",
    "description": "no where to go",
    "listId": 1,
    "sortOrder": 5,
    "deadline": "2022-01-29 14:53:01",
    "done": false
}
```
#### Check/Uncheck the item
```http
PUT /todolist/item/check/{itemId}
```
Note: Only update the isDone field the check/uncheck the item, imagine just clicking the checkbox on the item left hand side.
#### Move an item to other list of mine
```http
PUT /todolist/item/move
```
```json
{
    "itemId" : 2,
    "sortOrder" : 1,
    "targetListId" : 2
}
```
Note: the item will be only move within in the same list if **targetListId** is not being specified.

# Technical specifications
This application is written in JAVA and using Spring Boot 2.6.3 as the basement.
For the implementation I used.
- Spring MVC for RESTFul API
- Spring Data JPA for the data control.
- Spring AOP to implement the RBAC.
- Mockito for unit test.

Besides, I am also using following tools/frameworks to implement the functionalities that I need.
- Liquibase to initiate the DB Schema, and also pre-populate the data for demonstration.
- H2 in memory DB for the DAO unit tests.
- Swagger to generate the API document automatically.

Thanks for your watching, Have a good day!
