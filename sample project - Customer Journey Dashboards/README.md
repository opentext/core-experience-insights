# expctr-sample-report

This project is home to a Spring-Boot server and the corresponding ReactJS front-end code. 

Building is done with maven. To assemble the spring boot app (creates exectuable jar) run `mvn clean install`. It also scans java code for the proper copyright headers.

## Spring Boot UI
The spring boot app is in the ./spring-boot-ui directory. Its main function is to spawn outbound REST requests to the configured report server.

This application uses Spring profiles to drive endpoint configuration. Profile names can be set via a command-line arg:  -Dspring-boot.run.profiles=dev. Otherwise, it'll run as the default, `production`.

### Building
To build this app locally: `mvn clean install`

### Tests
Tests can be run directly via `mvn test`. 

The report server is configured in the src/main/resources/application.yml. The current port is 8021. Overrides to the configuration should be done via `application-<profile>.yml`

### Starting
There are various ways to start the spring boot server. 

Via maven: `mvn spring-boot:start -Dspring-boot.run.profiles=<profile>`

Via executable jar: `java -jar target spring-boot-ui-1.0.0.jar`

A docker image in coming soon too.

To skip test execution at startup add the `-DskipTests` argument.

### Stopping
To stop this app (for now): `mvn spring-boot:stop` or via docker or other means.

## OAuth2 Configuration
Since this sample app makes an OAuth2-based REST call to the configured endpoint, the coordinates for that server need to be configured. To do that in development, create a new file called application-dev.yml in the src/main/resources directory and override the corresponding OAuth2 values as well as the REST endpoint itself:

```
api-server:
  scheme: https
  host: myreportserver.com
  port: 443
  context: /
  oauth2:
    client-id: myclientid
    client-secret: myclientsecret
 ```

This can then be applied using a profile argument in the mvn startup command:
`mvn spring-boot:start -Dspring-boot.run.profiles=dev`

When this application is deployed, these values will be set using a production yml file instead (per OTX k8s standards, etc).


## ReactJS
The React JS code is in the `reactJs` directory. This is for building the compiled app.min.js and other UI assets that are deployed to the spring-boot-ui resources directory. Working with this directory is not needed to run the application. It is used strictly for UI development.

### IDE
Use Microsoft Visual Code. The formatting configuration files are only designed for this IDE.

### Initializing
You may use npm instead of yarnJS. It is recommended you use windows cmd for these steps. To build the node_modules directory from the package.json, run `yarn install`

### Building
To build this app: `yarn build`

This will write to the spring-boot src/main/resources/static folder. To see changes reflected, rebuild and restart the spring-boot app

### Hot Development
The React Dev server is running on port 5000. This will do automatic page refreshes whenever UI code is modified. To start only that server, first start spring-boot, then type `yarn start`

### UI Automation Tests
To run automation tests: `yarn test:ui` or to run them with an interactive browser, `yarn test:uidev`
