/**
 * @name personal
 * @description Feedback
 * @link http://localhost:9514/feedback-survey/feedback/personal
 *
 * What was the main thing you needed to do today?
 *  - [x] #neededToDo
 *  - 'Write anything in here as long as it doesn’t go over 200 characters.'
 * How easy was it for you to do what you needed to do today?
 *  - [x] #howEasyQuestion-VeryEasy
 *  - [ ] #howEasyQuestion-Easy
 *  - [ ] #howEasyQuestion-Moderate
 *  - [ ] #howEasyQuestion-Difficult
 *  - [ ] #howEasyQuestion-VeryDifficult
 * Why did you give this score?
 *  - [x] #whyGiveScore
 *  - 'Write anything in here as long as it doesn’t go over 1,000 characters.'
 * Overall, how did you feel about the service you received today?
 *  - [x] #howDoYouFeelQuestion-VerySatisfied
 *  - [ ] #howDoYouFeelQuestion-Satisfied
 *  - [ ] #howDoYouFeelQuestion-Moderate
 *  - [ ] #howDoYouFeelQuestion-Dissatisfied
 *  - [ ] #howDoYouFeelQuestion-VeryDissatisfied
 **/
module.exports = {
  id: 'feedback-frontend--personal',
  scenarios: [
    {
      clickSelectors: [
        '#ableToDo-yes',
        '#howEasyQuestion-VeryEasy',
        '#howDoYouFeelQuestion-VerySatisfied',
      ],
      cookiePath: 'backstop_data/engine_scripts/cookies.json',
      delay: 300,
      expect: 0,
      hideSelectors: ['.cbanner-govuk-cookie-banner'],
      hoverSelector: '',
      keyPressSelectors: [
        {
          keyPress:
            'Write anything in here as long as it doesn’t go over 200 characters.',
          selector: '#neededToDo',
        },{
          keyPress:
            'Write anything in here as long as it doesn’t go over 1,000 characters.',
          selector: '#whyGiveScore',
        },
      ],
      label: 'Feedback frontend: personal',
      misMatchThreshold: 0.1,
      postInteractionWait: 300,
      readyEvent: '',
      readySelector: '.js',
      referenceUrl: '',
      removeSelectors: ['.cbanner-govuk-cookie-banner'],
      requireSameDimensions: true,
      scrollToSelector: '',
      selectorExpansion: true,
      selectors: ['document'],
      url: 'http://localhost:9514/feedback-survey/feedback/personal',
    },
  ],
};
