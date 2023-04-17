# Core Experience Insights – Analysis Query API
## Examples of CXI Analysis Query API calls

### Description

The CXI Postman Collection is designed to explain how to construct and use queries through the Analysis Query API.

### Outline of outcome 

API Calls will only show a result if you have created data sets of the type journey visualization in CXI with assigned data points as stages of the journey. All query results contain a maximum output of 20 results.


#### Get OAuth Token 
Gets the bearer token with your connection settings stored in the variables. Needed for the other queries to be executed against CXI Analysis Query API.

- Query - Journey Ids:  Shows the use of a simple SQL Statement with a Group By clause. Provides a list of Journey Ids as output.
- Query – Journey Stage Details: Shows the use of a simple SQL Statement with multiple columns. Provides a list of journey stages with its assigned journey Id, journey name, stage order number, stage Id, and stage name.
- Query – Journey Stage Count: Shows the use of a simple SQL Statement on a different table. Provides the number of events received at a each stage of journeys.
- Query – Complex Scan – Max: Shows the use of the function Max() in an SQL Statement. Provides the maximum number of events (stagecount) received in a any stage of a journey.
- Query – Complex Scan – Sum: Shows the use of the function Sum() in an SQL Statement. Provides the sum number of all events received in a journey.

#### Environment variables that must be configured
- otdsAuthBaseUrl: https://otdsauth.ot2.opentext.com - change for other datacenters
- cxiRestApiBaseUrl: https://experiencecenterapi.ot2.opentext.com - change for other datacenters
- cxiQueryUrl: /api/v1/bd/analyses/query - don't change
- tenantId: - use your CXI Tenant ID
- clientId: - use your generated Client ID
- clientSecret: - use the respective secret for your Client ID
