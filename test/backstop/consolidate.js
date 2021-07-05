/* eslint-disable no-console, import/no-dynamic-require, global-require, no-param-reassign */
/**
 * Attempting to piece this together:
 * https://gerg.dev/2020/04/scaling-visual-testing-with-backstopjs-part-1/
 */
const { stringify } = require('flatted');
const backstop = require('backstopjs');
const chalk = require('chalk');
const fs = require('fs');
const glob = require('glob');
const path = require('path');
const baseConfig = require('./backstop.json');

const pattern = './test/backstop/scenarios/**.js';
const configFiles = glob.sync(pattern);

const getScenarios = (config) => {
  const { scenarios } = config;

  // pull out all the properties in this config other than scenarios
  const meta = {};

  Object.entries(config).forEach((prop) => {
    console.info({ prop });
    if (
      Object.prototype.hasOwnProperty.call(config, prop) &&
      prop !== 'scenarios'
    ) {
      meta[prop] = config[prop];
    }
  });

  // now apply those properties to the scenarios
  return scenarios.map((scenario) => ({ ...meta, ...scenario }));
};

const allScenarios = configFiles.reduce((scenarios, filename) => {
  const thisPath = path.join(process.cwd(), filename);
  const thisConfig = require(thisPath);
  const theseScenarios = getScenarios(thisConfig);

  theseScenarios.forEach((scenario) => {
    const labelPrefix = filename.replace(/^\.\//, '').replace(/^\//, '');
    scenario.label = `${labelPrefix}/${scenario.label}`;
  });

  scenarios.push(...theseScenarios);
  return scenarios;
}, []);

const consolidate = {
  ...baseConfig,
  scenarios: allScenarios,
};

fs.writeFile('./test/backstop/journeys.json',
  JSON.stringify(consolidate, null, 2),
  () => console.log('Config written to ./test/backstop/journeys.json')
);

backstop('test', { config: consolidate }).then(() => console.log('Done.'));

exports.consolidate = consolidate;
