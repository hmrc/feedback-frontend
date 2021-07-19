# Visual Regression Testing

Use Cypress to check whether your commits have made any changes that they shouldn’t have.

## Running Cypress

You can run Cypress two ways:

1. On the command line (`cypress run`).
2. Using the app (`cypress open`).

This provides the results in two different ways.

### Command line

Run `npm test` from the command line and everything happens on the command line. This version outputs videos to `cypress/videos` (not kept in version control), so that you have a chance to see the automation working.



### Cypress app

Run `npm run cypress:open` to invoke the Cypress app, and from there you can run the tests that are in `cypress/integration`.

Either press “Run all specs”, or run an individual test by clicking on it. This version opens a browser (selectable in the Cypress app), and runs the tests as you watch.


## Reporting snapshots

Run `npm run cypress:report` to report snapshot diffs in your test results. This adds `--reporter cypress-image-snapshot/reporter` to the initial `npm test` command.

## Preventing failures

Run `npm run cypress:prevent` to prevent test failures when an image diff does not pass. This adds `--env failOnSnapshotDiff=false` to the initial `npm test` command.

## Reporting all failing snapshots

Run `npm run cypress:reportfails`. This combines the previous two commands by addings `--reporter cypress-image-snapshot/reporter --env failOnSnapshotDiff=false` to the initial `npm test` command.

## Updating snapshots

Run `npm run cypress:update` to update the base image files for all of your tests. This adds `--env updateSnapshots=true` to the initial `npm test` command.

## Updating Cypress

Do not be tempted by “New updates are available” at the bottom of the Cypress window. At the time of writing, Cypress is at version `7.7.0`, but `cypress-image-snapshot` has a dependency of `4.12.1`.

## Snapshot options

These are the default options when screenshots are diffed, but you can read more in the [Cypress Image Snapshot’s options](https://github.com/jaredpalmer/cypress-image-snapshot#options) section.

```js
addMatchImageSnapshotCommand({
  failureThreshold: 0.03, // threshold for entire image
  failureThresholdType: 'percent', // percent of image or number of pixels
  customDiffConfig: { threshold: 0.1 }, // threshold for each pixel
  capture: 'viewport', // capture viewport in screenshot
});
```

## Further documentation

- [Cypress on GitHub](https://github.com/cypress-io/cypress)
- [Cypress API](https://docs.cypress.io/api/table-of-contents)
- [Cypress Image Snapshot](https://github.com/jaredpalmer/cypress-image-snapshot)
- [Testing workshop Cypress](https://github.com/cypress-io/testing-workshop-cypress)
- [Mocha](https://mochajs.org/) & [Chai](https://www.chaijs.com/) (which are the basis for Cypress tests)