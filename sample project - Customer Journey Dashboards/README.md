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

To skip test execution at startup add the `-DskipTests` argument.

### Stopping
To stop this app (for now): `mvn spring-boot:stop` or via docker or other means.


## Configuration

Since this sample app makes both an OAuth2-based REST call to an OTDS endpoint and authenticated REST call to an API gateway, the coordinates for those servers need to be configured. This can be done by either creating a new profile yaml file called application-user.yml in the src/main/resources directory or editing the default application.yml file. Just override the corresponding configuration values as shown below. 

Note if using a custom profile, include a profile argument in the mvn startup command:
`mvn spring-boot:start -Dspring-boot.run.profiles=user`


### Variables that must be configured for OCP on OT
In the application-production.yml file (or whatever profile is being used), set the following lines:

```
api-server:
- journey-url: https://experiencecenterapi.ot2.opentext.com - change for other datacenters
- auth-url: https://otdsauth.ot2.opentext.com - change for other datacenters
- tenant-id: - use your Tenant ID.
- client-id: - use your generated Client ID
- client-secret: - use the respective secret for your Client ID
```

### Variables that must be configured for OCP on GCP
In the application-gcp.yml file (or whatever profile is being used), set the following lines:

```
api-server:
- api-host: ca.api.opentext.com - change for other regions
- tenant-id: use your Tenant ID
- client-id: use the value of confidential credentials - client_id from the Extending App Details Form in the Admin Center
- client-secret: use the value of confidential credentials - client_secret from the Extending App Details Form in the Admin Center
```
### Query

The default API query body is
```
{
        select : [
            {
                field : 'journey_id',
            },
            {
                field : 'journey_name',
            },
        ],
        from    : 'otec_bd_journey_stage_details',
        groupBy : ['journey_id'],
    }
```

This can be changed inside the JavaScript. See `DatasetReport.js`. For other API queries, see the Swagger page on the corresponding API server.

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
