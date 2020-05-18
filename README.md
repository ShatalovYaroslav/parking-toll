# **yamt** (yet another microservice template)
Restful microservice template

## Purpose

The purpose of the microservice template is to have common template for new microservice's implementation.

## Specific modifications
The following modifications are needed before starting to work on the micro-service:
* In the **build.gradle** file update the dependencies versions
* In the **src/main/resources/log4j2.xml** change the log file name from 'micro-service.log'
* In the **gradle.properties** modify the version, projectName, packageLastPartName (the last part in package name)
* In the **src/main/webapp/WEB-INF/applicationContext.xml** specify the correct package name in base-package (change the microservice_template name to the packageLastPartName already specified in properties)
* In the main/java modify the **package name** to your need.
These changes will impact at the springBoot section in build.gradle file. By default the Application.java from this package will start on bootRun.

## Prepare your IDE
In the project is used Lombok library. It is needed to install plugin to be able to use it.
In the IntelliJ IDEA to be able to see Lombok generated code, you should enable the annotation preprocessor. [See documentation here.](https://www.jetbrains.com/help/idea/2016.1/configuring-annotation-processing.html) (Settings->Build->Compiler->Annotation Processors: Enable annotation preprocessing)

## Building and running the micro-service

You can start a microservice as a standalone application:
```
$ gradlew clean build bootRun
```

You can build a WAR file as follows:

```
$ gradlew clean build war
```

Then, you can directly deploy the service with embedded Tomcat:

```
$ java -jar build/libs/microservice-template-X.Y.Z-SNAPSHOT.war
```

The WAR file produced by Gradle can also be deployed in the embedded Jetty container started by an instance of [ProActive Server](https://github.com/ow2-proactive/scheduling).

Sometimes the gradle processes are not killing properly when you stop the running application. If you receive the message "the port is already in use" on starting microservice, then kill all suspending gradle processes for previous task. You can do it manually or use in IntelliJ IDEA Gradle killer plugin.

## Example
The template is organized with a complete RESTful example. The example follows MVC packaging structure and covered by tests.
The purpose of example to make more easy developing of new service.
To try it out use Swagger or (http://localhost:8080/users).<br>

## Swagger

Available resources can be listed and tested with Swagger. The associated code is in the **Application.java** file:
Modify the name of microservice-template in title, description, licenseUrl, groupName sections. Put right allowedPaths.<br>
To access Swagger API:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


## Testing

In order to follow the best testing practises it is included testing part of Spring components with Mockito.<br>
For integration test it is provided code in SpringUserRestTest class. For testing REST methods is used RestTemplate from spring framework.<br>
You can also use Rest Assured library, that is already included in list of imported libraries in the gradle file.
