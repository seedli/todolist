# Tom's Todolist api demo
This is my api demo work to demostrate the manipulation of the todo lists.

## prerequisite
**Jdk11** and **Maven**, **Docker** is required.
## steps to run up this program
Please follow the following steps to run up this application.
### 1. create a MySql instance
This application is running based on a MySql database, please start a instance by using Docker at first.
```bash
docker network create demo-network
docker run --name mysql --network demo-network -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=demoDB -d mysql:8
```
I created a new network as **demo-network** in order to let the followed up application instance can be involved inside the same network to make them can be communicated each other.
### 2. clone and build this repository
Clone this repository and switch to the root directory of it, then use the maven the build up this application.
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
docker run --name demoApp --network demo-network -e RDS_HOSTNAME=mysql -p 8080:8080 -d demo-app
```
Of course, you can modify the port **-p [port]:8080** as your desired
