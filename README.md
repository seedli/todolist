# todolist demo

# create mysql container
docker network create demo-network
docker run --name mysql --network demo-network -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=demoDB -d mysql:8

# build demoApp image and create a container
mvn clean install -DskipTests
docker build -t demo-app .
docker run --name demoApp --network demo-network -e RDS_HOSTNAME=mysql -p 80:8080 -d demo-app