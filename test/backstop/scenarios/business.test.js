/**
 * @name business
 * @description Feedback
 * @link http://localhost:9514/feedback-survey/feedback/business
 *
 * What was the main service you used today
 * - [ ] #mainService-SelfAssessment
 * - [ ] #mainService-PAYE
 * - [x] #mainService-VAT
 * - [ ] #mainService-CorporationTax
 * - [ ] #mainService-CIS
 * - [ ] #mainService-ECSales
 * - [ ] #mainServiceOtherInput
 * Were you able to do what you needed to do today?
 *  - [ ] #ableToDo-yes
 *  - [x] #ableToDo-no
 * How easy was it for you to do what you needed to do today?
 *  - [ ] #howEasyQuestion-VeryEasy
 *  - [ ] #howEasyQuestion-Easy
 *  - [ ] #howEasyQuestion-Moderate
 *  - [x] #howEasyQuestion-Difficult
 *  - [ ] #howEasyQuestion-VeryDifficult
 * Why did you give this score?
 *  - [x] #whyGiveScore
 *  - 'Write anything in here as long as it doesn’t go over 1,000 characters.'
 * Overall, how did you feel about the service you received today?
 *  - [ ] #howDoYouFeelQuestion-VerySatisfied
 *  - [ ] #howDoYouFeelQuestion-Satisfied
 *  - [ ] #howDoYouFeelQuestion-Moderate
 *  - [x] #howDoYouFeelQuestion-Dissatisfied
 *  - [ ] #howDoYouFeelQuestion-VeryDissatisfied
 **/
module.exports = {
  id: 'feedback-frontend--business',
  scenarios: [
    {
      clickSelectors: [
        '#mainServiceQuestion-VAT',
        '#ableToDo-no',
        '#howEasyQuestion-Difficult',
        '#howDoYouFeelQuestion-Dissatisfied',
      ],
      cookiePath: 'backstop_data/engine_scripts/cookies.json',
      delay: 300,
      expect: 0,
      hideSelectors: ['.cbanner-govuk-cookie-banner'],
      hoverSelector: '',
      keyPressSelectors: [
        {
          keyPress:
            'Write anything in here as long as it doesn’t go over 1,000 characters.',
          selector: '#whyGiveScore',
        },
      ],
      label: 'Feedback frontend: business',
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
      url: 'http://localhost:9514/feedback-survey/feedback/business',
    },
  ],
};
