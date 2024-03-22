# Projectinformation

## Version infos
- Java Version 18
- Maven Version 3..8.6

## Server

### install all dependencies
`mvn install -f pom.xml`

### run the server
`mvn spring-boot:run`

### command for docker
#### for windows
`docker run -it --name maven -p 8080:8080 -v .:/app -w /app maven bash`

#### for mac/linux
`docker run -it --name maven -p 8080:8080 -v ${PWD}:/app -w /app maven bash`

### command to start and run
`docker start maven`

`docker exec -it maven bash`

## Client

### install all dependencies
`npm install`

### run the server
`npm run dev`

### command for docker
#### for windows
`docker run -it --name node -p 8081:8081 -v .:/app -w /app node bash`

#### for mac/linux
`docker run -it --name node -p 8081:8081 -v ${PWD}:/app -w /app node bash`

### command to start and run
`docker start node`

`docker exec -it node bash`
