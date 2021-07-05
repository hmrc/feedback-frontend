/**
 * @name pension
 * @description Feedback
 * @link http://localhost:9514/feedback-survey/feedback/pension
 *
 * Were you able to do what you needed to do today?
 *  - [x] #ableToDo-yes
 *  - [ ] #ableToDo-no
 * How easy was it for you to do what you needed to do today?
 *  - [ ] #howEasyQuestion-VeryEasy
 *  - [ ] #howEasyQuestion-Easy
 *  - [x] #howEasyQuestion-Moderate
 *  - [ ] #howEasyQuestion-Difficult
 *  - [ ] #howEasyQuestion-VeryDifficult
 * Why did you give this score?
 *  - [x] #whyGiveScore
 *  - 'Write anything in here as long as it doesn’t go over 1,000 characters.'
 * Overall, how did you feel about the service you received today?
 *  - [ ] #howDoYouFeelQuestion-VerySatisfied
 *  - [ ] #howDoYouFeelQuestion-Satisfied
 *  - [x] #howDoYouFeelQuestion-Moderate
 *  - [ ] #howDoYouFeelQuestion-Dissatisfied
 *  - [ ] #howDoYouFeelQuestion-VeryDissatisfied
 * What are you most likely to do after checking your State Pension?
 *  - [ ] #likelyToDoQuestion-OtherPensions
 *  - [ ] #likelyToDoQuestion-CheckFinances
 *  - [ ] #likelyToDoQuestion-ClarifyInformation
 *  - [x] #likelyToDoQuestion-GetProfessionalAdvice
 *  - [ ] #likelyToDoQuestion-DoNothing
 **/
module.exports = {
  id: 'feedback-frontend--pension',
  scenarios: [
    {
      clickSelectors: [
        '#ableToDo-yes',
        '#howEasyQuestion-Moderate',
        '#howDoYouFeelQuestion-Moderate',
        '#likelyToDoQuestion-GetProfessionalAdvice'
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
      label: 'Feedback frontend: pension',
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
      url: 'http://localhost:9514/feedback-survey/feedback/pension',
    },
  ],
};
