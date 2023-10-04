# Demo project that demonstrates microservice architecure with Spring Boot 3 and gRPC

## Preface

Creating a microservice architecture is always a challenging thing. First you have to analyze carefully the data flows and the work flows. There are many ways how you can implement microservice comminications. But there are two most popular procedures and those are:

* Event driven architecture
* RPC

### Event driven architecture

This architecture requires a message bus. Usually, that is some kind of MQ server or Kafka. In short: The microservices will communicate with each other by pushing/producing a message into the message bus. Other microservices will consume these messages and they will send the result back to the message bus for other services. Also if there is an error in the service the error is also pushed to message bus so the other services will get information about the problem. You can find event-driven demo implementations here:

* Java: https://github.com/pbakota/java-microservice-kafka
* C# : https://github.com/pbakota/mt-microservice-kafka

### RPC

We can say that the event-driven architecture uses asynchronous communication, and the RPC (Remote procedure call) method uses synchronous. When you work with RPC calls it almost will look like you are working with local methods. However, if you want to use this kind of communication you have to have prepared *.proto files and generated client/server stubs. Fortunately, maven has an excellent package that will generate for you automatically the client and the server stubs.

## The architecture

![Alt text](https://github.com/pbakota/java-microservices-grpc/blob/main/figures/figure-1.svg)

The demo implements RPC communication between microservices.

If you compare this architecture with the architecture that uses Event-driven design, you will find they are very similar. For RPC implementation I used gRPC with Protobuffer. https://protobuf.dev/

Except for the communication, other parts are almost exactly the same. I also implemented a simple API GW again with Spring Cloud Gateway. https://spring.io/projects/spring-cloud-gateway. Again just to demonstrate how easy is to have an API GW in front of your microservices.

## Implementation

The whole demo project was implemented in Java 17, with Spring Boot 3, Google Protobuf, and Grpc. Each microservice has its own project folder. There is a shared project called 'common', which is shared between all microservices and contains the common structures. There is another important folder and that is 'protobuf', this folder contains all the required *.proto files, used for Protobuffer and Grpc tool to generate stubs.

The serives are:

* *orders* - Contains project for handling orders
* *payments* - Contains project for handling payments
* *stock* - The stock management
* *delivery* - The delivery management. Currently only acknowledges the delivery.
* *gw* - Contains the project for API gw

In the folder 'http' in each project, you can find HTTP requests used by the VSCode plugin `REST Client` https://marketplace.visualstudio.com/items?itemName=humao.rest-client

The microservices use shared PostgreSQL. But each of them can have its own separate databases, to demonstrate that they are completely independent from each other.

Also, the project contains all required Docker files for deployment.


## Build & run the project

The project used GNU make files to build and package the services. There are a couple of targets that can be used:

* `$ make` - This will build each microservice
* `$ make run-orders` - This will run the orders microservice
* `$ make run-payments` - This will run the payments microservice
* `$ make run-stock` - This will run the stock microservice
* `$ make run-delivery` - This will run the delivery microservice
* `$ make run-api-gw` - This will run the API gateway

To test the whole demo project you have to have all the services up and running.

## Deploying in Docker container

You have to build the whole project and then copy the microservice JAR file into their matching folder under the 'docker' folder.

Run the stack with the command
```
$ make start-stack
```

Or you can stop it with command
```
$ make stop-stack
```

# Footnote

Do not forget to update your application.properties file under 'docker' container (or in microservce's resources folder during development).

Simillar project for C# can be found here: https://github.com/pbakota/csharp-microservices-grpc
