# Visual Regression Testing

Upon cloning, run `npm install` to set things up, and then `npm test`. This will run the linting tests and an initial backstop test.

A freshly-cloned repo *should* run the first test without issue. 🤞

Once you start making changes and you’re ready to commit them, run `npm test` again. You should see errors referencing the areas where your changes were made. Assuming these are the only changes which were flagged, you can approve them by running `npm run approve`. Once that has run and added them, run `npm test` again to test with those changes.

## Further commmands

- `npm start`: this creates reference images and initialises backstop in the service, which is already done.
- `npm run lint:js`: lint the JS files with ESLint.
- `npm run lint:scss`: lint the SCSS files with Stylelint.

Note: lint checks must resolve in order to commit code.

## BackstopJs files

Explanation of the files, of which only `bitmaps_reference` and `engine_scripts` are committed:
```sh
/backstop_data
  bitmaps_reference/            # Reference images live in here.
  bitmaps_test/                 # Any tests you run; images & report.json.
  engine_scripts/               # These scripts are in use by BackstopJs, so do not
    puppet/                     # delete them or amend them too heavily. See below.
      overrideCSS.js            #
      onReady.js                # These files are mostly self-explanatory
      onBefore.js               # by the file names alone, but should be
      loadCookies.js            # left unedited if possible, as updating
      interceptImages.js        # BackstopJS would wipeout the changes.
      ignoreCSP.js              # See below for advice about amends.
      clickAndHoverHelper.js    #
    cookies.json                # Add any cookie info needed to complete the journey.
  html_report/                  # Report showing all images.
```

For clarity: the files in the `puppet` directory are **required**—not *optional*.

### Updating the puppet files

Rather than having a BackstopJS update overwrite your puppet changes and have to dig them out of version control, the better way to approach updates to these files is to create your own directory and copy the files into there before amending them.
```sh
mkdir backstop_data/my_custom_engine_scripts
cp -R backstop_data/engine_scripts/ backstop_data/my_custom_engine_scripts/
```
Then, you can change the directory value in the `backstop.json` configuration file:
```json
"engine_scripts": "backstop_data/my_custom_engine_scripts"
```

## Cookies

If you need to add cookies to `cookies.json` (for example,  from `auth-login`), you can do that by exporting them from your browser with the [Export cookie JSON file for Puppeteer](https://chrome.google.com/webstore/detail/%E3%82%AF%E3%83%83%E3%82%AD%E3%83%BCjson%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB%E5%87%BA%E5%8A%9B-for-puppet/nmckokihipjgplolmcmjakknndddifde?hl=en). If the plugin goes missing, or you need further help with this, check [BackstopJS GitHub Issue #1238: "Cookie Inspector" Chrome Extension no longer exists](https://github.com/garris/BackstopJS/issues/1238).

## Config

### Multiple scenarios

Multiple scenarios can be easily handled in the default JSON configuration.

```json
scenarios: [{
  "label": "Page one",
  "…"
},{
  "label": "Page two",
  "…"
}]
```

### Multiple journeys

This will involve multiple files, which are best split up as individual files and included into a JS-based config file, rather than the default JSON one. You can find examples of this in the [scenarios](test/backstop/scenarios) directory, where there are multiple files for the beta journey, covering different ratings.

### Scenario properties

Scenario properties are described throughout [this document](https://github.com/garris/BackstopJS#readme) and processed sequentially in the following order...

| Property | Description |
| -------- | ----------- |
| `label` | [**required**] Tag saved with your reference images.|
| `onBeforeScript` | Used to set up browser state, for example cookies.|
| `cookiePath` | Import cookies in JSON format.|
| `url` | [**required**] The `url` of your app state.|
| `referenceUrl` | Specify a different state or environment when creating reference.|
| `readyEvent` | Wait until this string has been logged to the console.|
| `readySelector` | Wait until this selector exists before continuing.|
| `delay` | Wait for `x` milliseconds|
| `hideSelectors` | Array of selectors set to `visibility:hidden`|
| `removeSelectors` | Array of selectors set to `display:none`|
| `onReadyScript` | After the above conditions are met -- use this script to modify UI state prior to screen shots, for example hovers, clicks etc.|
| `keyPressSelectors` | Takes array of selector and string values -- simulates multiple sequential keypress interactions.|
| `hoverSelector` | Move the pointer over the specified DOM element prior to screen shot.|
| `hoverSelectors` | [*Puppeteer only*] Takes array of selectors -- simulates multiple sequential hover interactions.|
| `clickSelector` | Click the specified DOM element prior to screen shot.|
| `clickSelectors` | [*Puppeteer only*] Takes array of selectors -- simulates multiple sequential click interactions.|
| `postInteractionWait` | Wait for a selector after interacting with hoverSelector or clickSelector (optionally accepts wait time in ms. Ideal for use with a click or hover element transition. available with default onReadyScript)|
| `scrollToSelector` | Scrolls the specified DOM element into view prior to screen shot (available with default onReadyScript)|
| `selectors` | Array of selectors to capture. Defaults to document if omitted. Use "viewport" to capture the viewport size. See Targeting elements in the next section for more info...|
| `selectorExpansion` | See Targeting elements in the next section for more info...|
| `misMatchThreshold` | Percentage of different pixels allowed to pass test|
| `requireSameDimensions` | If set to true -- any change in selector size will trigger a test failure.|
| `viewports` | An array of screen size objects your DOM will be tested against. This configuration will override the viewports property assigned at the config root.|
