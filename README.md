# Parking Toll Library
Parking Toll Library implemented in Java using Spring exposing REST API

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
The Parking Lot can have different prices for Parking Spots according to Spot's Vehicle Type.
<br>The service allows to place the vehicle only in existing Parking Lots. The service API allows to get Parking Lots ID.
The Parking Lot ID should be provided during the vehicle parking to place a vehicle into a specific Parking Lot.

#### Creation of a Test Parking Lot on service startup
<br> By default on the service startup the testing Parking Lot will be created. A user can disable this option by changing it in "applications.properties" file  with next value: 'create.testing.parking=false'.
This option is only for testing purpose (to make easier the verification), please change the parameter to 'false' value.
WARNING: The parking lot data is persisted in DB. If the property value is 'create.testing.parking=true', every time when the service starts it will be created the new test parking lot.

### Description of Parking Spot
A Parking Lot contains multiple parking slots of different types. The Parking Service supports 3 types of Parking Spots:
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

#### How to create the Parking Lot
To create a new Parking Lot it should be provided: 
- key-values Map for Spot prices for Vehicle Type (each Vehicle Type can have different price) 
- key-values Map for amount of Spots for each Vehicle Type.

The REST API of service is communicating via JSON. You need to send the POST request with required JSON to next API endpoint:
```
http://localhost:8080/parking/lot/
```
The JSON example to create a Parking Lot is following: 
```
{
  "name": "test-parking-lot",
  "policyType": "FIXED_PLUS",
  "priceByVehicleType": {"gasoline":1.4, "twenty_kw":2.7, "fifty_kw":3.7},
  "pricingParameters": {"fixedPrice":9.99},
  "spotsNumberByType": {"gasoline":2, "twenty_kw":2, "fifty_kw":1}
}
```

## Data persistence in DB
The detailed information about Parking Lots and Parking Spots are persisted in DB. By default it is used the automatically created HSQL DB.
<br>A user of the micro-service can configure to use the other existing DB by changing the Data source properties in the "applications.properties" file.
<br> The default configured HSQL DB will be created in temp folder of OS (according to "java.io.tmpdir"). The DB files will be stored inside 'data/parking/' folder.
<br> A user can control the path folder for default DB with setup a 'parking.home' system property.

## Structure
The service is organized with a REST API. The service follows MVC packaging structure and covered by tests.
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

## Assumptions

In the real project all the questions should be clarified and discussed on the design stage. 
In this exercise it is taken the next assumptions:

- The parking rent time is calculated by hours the vehicle stays in the parking spot
- The first hour in the parking spot it is free of charge
- For the 'park vehicle' and 'leave parking' actions: the vehicle information is passed to this service via API.
- When a vehicle leaves a parking, it will be provided the parking invoice. The invoice payment is out of scope of this service.
- The main model classes are inside "model.persistence" package. 
To avoid code duplication in this exercise the same main model classes representing internal model and DB entities.
In real life project the internal model classes and entity classes for DB could be separated in different packages.
The trade offs should be clarified and discussed for real use case.

## Future improvements

- It can be added the authentication mechanism. The API endpoints could be secured, so it will be difficult to exploit them
- To improve the response time to get a parking spot it can be implemented a caching mechanism with Hash Map data structure
- The API endpoint to 'Get information about all parking lots' can be paginated
- It could be added a rate limit control so the API users won’t overuse the API