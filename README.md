
# feedback-frontend

Services can integrate with Feedback frontend by navigating the URL with a unique name for your service for the survey you need.

Currently, there are 2 surveys: a survey for personal tax accounts, and a generic survey. Each service will be responsible for choosing which survey applies to them.

## Endpoints

The endpoints are as follows:

| Production, qa, staging | Local development |
|-----------------------|-------------------|
|**Generic:**               |**Generic:**           |
|`/feedback/SERVICE_NAME`|`http://localhost:9514/feedback/SERVICE_NAME`|
|**Personal tax accounts:**|**Personal tax accounts:**|
|`/feedback/SERVICE_NAME/personal`|`http://localhost:9514/feedback/SERVICE_NAME/personal`|

Replace `SERVICE_NAME` with the identifier for your service.

## Service manager

To run via service manager: `sm --start FEEDBACK_FRONTEND -r`

You will need to ensure that you do not have `FEEDBACK_SURVEY_FRONTEND` running as they use the same port number.

You will need to update your service manager config for your service and replace `FEEDBACK_SURVEY_FRONTEND` with `FEEDBACK_FRONTEND`

## Usage

When redirecting the user to the feedback service you should ensure that the user has been logged out as the feedback service does not do this.

Log out user and redirect:

```sh
Redirect("http://localhost:9514/feedback/SERVICE_NAME").withNewSession
```

If you need additional information audited you may pass through an optional `feedbackId` variable as a session value, you should ensure that this is unique as this will be audited alongside the user responses so they can be collated in Splunk.

Example:

```sh
val uuid = randomUUID().toString

val auditData = Map("feedbackId" -> uuid, "customMetric" -> "value")

auditConnector.sendExplicitAudit("service-name", auditData)

Redirect("http://localhost:9514/feedback/SERVICE_NAME").withSession(("feedbackId", uuid))
```

## Visual Regression Testing

BackstopJS is configured in this repository.

Upon cloning, run `npm install` to set things up, and then `npm test`.

Any changes you make after that can be approved with `npm run approve`, and then run `npm test` again to test with those changes.

There are some further notes in the [backstop directory](./test/backstop/README.md).

## VS Code additions

To help those in design and front-end who use VS Code instead of IntelliJ, there are some additional files in this repository.

- `extensions.json`: recommends a set of extensions to help with a Scala development environment, as well as debugging and linting tools, and things to generally help you commit more consistent code.
  **Run these from in the Workspace Recommendations section of the Extensions sidebar.**
- `launch.json`: a set of debug configurations to help debug your code with the help of various browsers and tools.
  **Run these from in the Debug sidebar.**
- `tasks.json`: some `sbt` tasks to manage the service and get it running.
  **Run these from the Command Palette.** <kbd>cmd</kbd>+<kbd>p</kbd> `task sbt` will show you the available options.

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
