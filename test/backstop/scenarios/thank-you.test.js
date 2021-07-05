/**
 * @name thank-you
 * @description Feedback
 * @link http://localhost:9514/feedback-survey/feedback/thank-you
 **/
module.exports = {
  id: 'feedback-frontend--thank-you',
  scenarios: [
    {
      clickSelectors: [],
      cookiePath: 'backstop_data/engine_scripts/cookies.json',
      delay: 300,
      expect: 0,
      hideSelectors: ['.cbanner-govuk-cookie-banner'],
      hoverSelector: '',
      keyPressSelectors: [],
      label: 'Feedback frontend: thank-you',
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
      url: 'http://localhost:9514/feedback-survey/feedback/thank-you',
    },
  ],
};
