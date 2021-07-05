/**
 * @name give-comments
 * @description Feedback
 * @link http://localhost:9514/feedback-survey/feedback/give-comments
 *
 * What did you come to do?
 *  - [x] #value
 *  - 'Write anything in here as long as it doesn’t go over 1,000 characters.'
 **/
module.exports = {
  id: 'feedback-frontend--give-comments',
  scenarios: [
    {
      clickSelectors: [],
      cookiePath: 'backstop_data/engine_scripts/cookies.json',
      delay: 300,
      expect: 0,
      hideSelectors: ['.cbanner-govuk-cookie-banner'],
      hoverSelector: '',
      keyPressSelectors: [
        {
          keyPress:
            'Write anything in here as long as it doesn’t go over 1,000 characters.',
          selector: '#value',
        },
      ],
      label: 'Feedback frontend: give-comments',
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
      url: 'http://localhost:9514/feedback-survey/feedback/give-comments',
    },
  ],
};
