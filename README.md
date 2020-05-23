# Parking Toll Library
Parking Toll Library implemented in Java using Spring exposing RESTful API

## Purpose

The purpose of the service is to have the Parking logic implementation.

## How to use
To start the service, please take a look on "Building and running the parking micro-service" section.
This parking service is exposing REST API. 
A complete documentation of the Parking REST API is available by default on:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Description of the service
The parking service is dealing with multiple Parking Lots. Each Parking Lot is identified by parking Lot Id and contains Parking Spots.
This service allows to park the vehicle in concrete Parking Lot in the free parking spot, according to vehicle's type. 
When the Vehicle leaves Parking Lot: the parking service marks the parking slot as free and provide the Invoice with a cost and parking renting details.

### Description of Parking Lot
The Parking Lot should be created with certain Pricing Policy. The new Parking Lot can be added with REST Api. 
To create a new Parking Lot it should be provided: 
- key-values Map for Spot prices for Vehicle Type (each Vehicle Type can have different price) 
- key-values Map for amount of Spots for each Vehicle Type.

<br> By default on service startup the testing Parking Lot will be created. You can disable this option by changing in "applications.properties" file the value to 'create.testing.parking=false'
<br>The service allows to place the vehicle only in existing Parking Lots. The service API allows to get Parking Lots ID.
The Parking Lot ID should be provided during the vehicle parking to place a vehicle into a specific Parking Lot.

### Description of Parking Spot
A Parking Lot contains multiple parking slots of different types. The Parking Service support 3 types of Parking Spots:
- the standard parking slots for sedan cars (gasoline-powered) 
- parking slots with 20kw power supply for electric cars
- parking slots with 50kw power supply for electric cars

The Vehicle type should be specified during the parking process accordingly with next values: "gasoline", "twenty_kw", "fifty_kw".
<br>According to Vehicle type, a Vehicle can be parked only in the corresponding Parking Spot. 

### Description of Pricing Policy
Every Parking Lot is free to implement its own pricing policy. It should be specified the Pricing Policy Type during creation of a Parking Lot.
The Pricing Policy Type corresponds to Pricing Policy implementation.
 In current implementation it is supported next policies with corresponding Pricing Policy Type:
- "STANDARD" policy type. This Policy bills their customer for each hour spent in the parking (number hours * hour price)
- "FIXED_PLUS" policy type. It bills a fixed amount + each hour spent in the parking (fixed amount + number hours * hour price)

Each Pricing Policy can have additional parameters values, that depends on Policy strategy implementation.
For example for "FIXED_PLUS" Pricing Policy implementation it is required to provide "fixedPrice" parameter on creation.

#### Example how to create the Parking Lot
The REST API of service is communicating via JSON. You need to send the POST request with required JSON to next API endpoint:
```
http://localhost:8080/parking/lot/
```
The JSON example to create a Parking Lot is following: 
```
{
  "name": "test-parking-lot",
  "policyType": "FIXED_PLUS",
  "priceByVehicleType": {"gasoline":1.4, "fifty_kw":3.7},
  "pricingParameters": {"fixedPrice":9.99},
  "spotsNumberByType": {"gasoline":2, "fifty_kw":1}
}
```

## Structure
The service is organized with a complete RESTful API. The service follows MVC packaging structure and covered by tests.
The service is based on Spring framework and can be started with Spring Boot.
To try it out use Swagger or (http://localhost:8080/parking).<br>

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

