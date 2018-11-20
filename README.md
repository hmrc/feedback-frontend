
# feedback-frontend

Services can integrate with Feedback frontend by navigating the URL with a unique name for your service for the survey you need.

Currently there are 2 surveys, a survey for personal tax accounts and a generic survey. Each service will be responsible for choosing which survey applies to them.

The endpoints are:

#####production/qa/staging

Generic
```
/feedback/SERVICE_NAME
```

Personal tax accounts
```
/feedback/SERVICE_NAME/personal
```


#####local development
Generic
```
http://localhost:9514/feedback/SERVICE_NAME
```
Personal tax accounts
```
http://localhost:9514/feedback/SERVICE_NAME/personal
```

Replace `SERVICE_NAME` with the identifier for your service, 

####Service manager

To run via service manager: `sm --start FEEDBACK_FRONTEND`

You will need to ensure that you do not have `FEEDBACK_SURVEY_FRONTEND` running as they use the same port number.

####Usage

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
