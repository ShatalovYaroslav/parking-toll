# Parking Toll Library
Parking Toll Library implemented in Java using Spring exposing RESTful API

## Purpose

The purpose of the service is to have the Parking logic implementation.

## Structure
The service is organized with a complete RESTful API. The service follows MVC packaging structure and covered by tests.
The service is based on Spring framework and can be started with Spring Boot.
To try it out  use Swagger or (http://localhost:8080/parking).<br>

## Building and running the parking micro-service

You can start a parking service as a standalone application:
```
$ gradlew clean build bootRun
```

You can build a WAR file as follows:

```
$ gradlew clean build war
```

Then, you can directly deploy the service with embedded Tomcat:

```
$ java -jar build/libs/parking-X.Y.Z.war
```

The WAR file produced by Gradle can also be deployed in the embedded Jetty container.

Sometimes the gradle processes are not killing properly when you stop the running application. If you receive the message "the port is already in use" on starting microservice, then kill all suspending gradle processes for previous task. You can do it manually or use in IntelliJ IDEA Gradle killer plugin.

## Swagger

Available resources can be listed and tested with Swagger.
To access Swagger API:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Prepare your IDE for development
In the project it is used Lombok library. It is needed to install plugin to be able to use it.
In the IntelliJ IDEA to be able to see Lombok generated code, you should enable the annotation preprocessor. [See documentation here.](https://www.jetbrains.com/help/idea/2016.1/configuring-annotation-processing.html) (Settings->Build->Compiler->Annotation Processors: Enable annotation preprocessing)

## Testing

In order to follow the best testing practises it is included testing part of Spring components with Mockito.<br>
The integration tests are provided in 'integration-tests' folder and applied usual black-box testing practise. For testing REST methods is used RestAssured.<br>

