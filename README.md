
# feedback-frontend

Services can integrate with Feedback frontend by navigating the URL with a unique name for your service for the survey you need.

Currently there are 10 surveys, these are for:
* Personal Tax Account (PTA)
* Business Tax Account (BTA)
* Trusts
* National Minimum Wage (NMW)
* Customer Compliance Group (CCG)
* Pension
* Give Reason
* Give Comments
* Beta
* and a generic survey. Each service will be responsible for choosing which survey applies to them.

The endpoints are:

| Survey        | Dev/QA/Staging/Prod                  | Local                                                     |
|---------------|--------------------------------------|-----------------------------------------------------------|
| PTA           | /feedback/SERVICE_NAME/personal      | http://localhost:9514/feedback/SERVICE_NAME/personal      |
| BTA           | /feedback/SERVICE_NAME/business      | http://localhost:9514/feedback/SERVICE_NAME/business      |
| Trusts        | /feedback/trusts                     | http://localhost:9514/feedback/trusts                     |
| NMW           | /feedback/SERVICE_NAME/nmw           | http://localhost:9514/feedback/SERVICE_NAME/nmw           |
| CCG           | /feedback/SERVICE_NAME/ccg           | http://localhost:9514/feedback/SERVICE_NAME/ccg           |
| Pension       | /feedback/SERVICE_NAME/pension       | http://localhost:9514/feedback/SERVICE_NAME/pension       |
| Give Reason   | /feedback/SERVICE_NAME/give-reason   | http://localhost:9514/feedback/SERVICE_NAME/give-reason   |
| Give Comments | /feedback/SERVICE_NAME/give-comments | http://localhost:9514/feedback/SERVICE_NAME/give-comments |
| Beta          | /feedback/SERVICE_NAME/beta          | http://localhost:9514/feedback/SERVICE_NAME/beta          | 
 | Generic       | /feedback/SERVICE_NAME               | http://localhost:9514/feedback/SERVICE_NAME               |

Replace `SERVICE_NAME` with the identifier for your service.

#### Service manager

To run via service manager: `sm --start FEEDBACK_FRONTEND`

You will need to ensure that you do not have `FEEDBACK_SURVEY_FRONTEND` running as they use the same port number.

You will need to update your service manager config for your service and replace `FEEDBACK_SURVEY_FRONTEND` with `FEEDBACK_FRONTEND`

#### Usage

When redirecting the user to the feedback service you should ensure that the user has been logged out as the feedback service does not do this.

Log out user and redirect
```
Redirect("http://localhost:9514/feedback/SERVICE_NAME").withNewSession
```

If you need additional information audited you may pass through an optional `feedbackId` variable as a session value, you should ensure that this is unique as this will be audited alongside the user responses so they can be collated in Splunk.
Example:
```
val uuid = randomUUID().toString

val auditData = Map("feedbackId" -> uuid, "customMetric" -> "value")

auditConnector.sendExplicitAudit("service-name", auditData)

Redirect("http://localhost:9514/feedback/SERVICE_NAME").withSession(("feedbackId", uuid))
```
### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

