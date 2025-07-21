
# feedback-frontend

## Overview

The feedback service gives users the opportunity to provide feedback on their overall experience of a digital service.

The survey will be presented only if a user 'signs out' of the service they are using. For users who've used multiple services during the same session, the feedback will be counted against where they signed out from.

## Integration

Services can integrate with Feedback frontend by navigating the URL with a unique name for your service for the survey you need.

Currently, there are 11 surveys, these are for:
* Personal Tax Account (PTA)
* Business Tax Account (BTA)
* Trusts
* Store my NINO (SmN)
* National Minimum Wage (NMW)
* Customer Compliance Group (CCG)
* Pension
* Beta
* Complaints
* and a generic survey. Each service will be responsible for choosing which survey applies to them.

The endpoints are:

| Survey        | Dev/QA/Staging/Prod                  | Local                                                     |
|---------------|--------------------------------------|-----------------------------------------------------------|
| PTA           | /feedback/SERVICE_NAME/personal      | http://localhost:9514/feedback/SERVICE_NAME/personal      |
| BTA           | /feedback/SERVICE_NAME/business      | http://localhost:9514/feedback/SERVICE_NAME/business      |
| Trusts        | /feedback/trusts                     | http://localhost:9514/feedback/trusts                     |
| SmN           | /feedback/SERVICE_NAME/nino          | http://localhost:9514/feedback/SERVICE_NAME/nino          |
| NMW           | /feedback/SERVICE_NAME/nmw           | http://localhost:9514/feedback/SERVICE_NAME/nmw           |
| CCG           | /feedback/SERVICE_NAME/ccg           | http://localhost:9514/feedback/SERVICE_NAME/ccg           |
| Pension       | /feedback/SERVICE_NAME/pension       | http://localhost:9514/feedback/SERVICE_NAME/pension       |
| Beta          | /feedback/SERVICE_NAME/beta          | http://localhost:9514/feedback/SERVICE_NAME/beta          | 
| Complaints    | /feedback/com/SERVICE_NAME           | http://localhost:9514/feedback/com/SERVICE_NAME           |
| Generic       | /feedback/SERVICE_NAME               | http://localhost:9514/feedback/SERVICE_NAME               |

Replace `SERVICE_NAME` with the identifier for your service.

## Running locally

Application will run on port: 9514

To run via terminal: `sbt run`
To run via service manager: `sm --start FEEDBACK_FRONTEND`

## Testing

To run unit tests: `sbt test`
To run accessibility tests: `A11y/test`

## Usage

When redirecting the user to the feedback service you should ensure that the user has been logged out as the feedback service does not do this.

Log out user and redirect (amend URLs per env via config values)
```
Redirect("http://localhost:9553/bas-gateway/sign-out-without-state?continue=http://localhost:9514/feedback/SERVICE_NAME").withNewSession
```

If you need additional information audited you may pass through an optional `feedbackId` variable as a session value, you should ensure that this is unique as this will be audited alongside the user responses, so they can be collated in Splunk.
Example:
```
val uuid = randomUUID().toString

val auditData = Map("feedbackId" -> uuid, "customMetric" -> "value")

auditConnector.sendExplicitAudit("service-name", auditData)

Redirect("http://localhost:9553/bas-gateway/sign-out-without-state?continue=http://localhost:9514/feedback/SERVICE_NAME").withSession(("feedbackId", uuid))
```
## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

