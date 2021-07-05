/**
 * @name give-reason
 * @description Feedback
 * @link http://localhost:9514/feedback-survey/feedback/give-reason
 *
 * What did you come to do?
 *  - [ ] #giveReason-checkTaxCode
 *  - [ ] #giveReason-checkTaxYear
 *  - [ ] #giveReason-checkTaxPaid
 *  - [ ] #giveReason-claimTaxBack
 *  - [ ] #giveReason-contactAboutP800
 *  - [ ] #giveReason-p800Wrong
 *  - [ ] #giveReason-payOwedTax
 *  - [ ] #giveReason-progressChasing
 *  - [x] #giveReason-other
 *  - [x] #reasonInput
 *  - 'I came to complain about inaccessible input fields on feedback forms.'
 **/
module.exports = {
  id: 'feedback-frontend--give-reason',
  scenarios: [
    {
      clickSelectors: [
        '#giveReason-other'
      ],
      cookiePath: 'backstop_data/engine_scripts/cookies.json',
      delay: 300,
      expect: 0,
      hideSelectors: ['.cbanner-govuk-cookie-banner'],
      hoverSelector: '',
      keyPressSelectors: [
        {
          keyPress:
            'I came to complain about inaccessible input fields on feedback forms.',
          selector: '#reasonInput',
        },
      ],
      label: 'Feedback frontend: give-reason',
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
      url: 'http://localhost:9514/feedback-survey/feedback/give-reason',
    },
  ],
};
