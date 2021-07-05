/**
 * @name ccg
 * @description Feedback
 * @link http://localhost:9514/feedback-survey/feedback/ccg
 *
 *  How strongly do you agree or disagree that HMRC treated you professionally during the compliance check?
 *  - [ ] #treatedProfessionallyQuestion-StronglyAgree
 *  - [ ] #treatedProfessionallyQuestion-Agree
 *  - [x] #treatedProfessionallyQuestion-NeitherAgreeNorDisagree
 *  - [ ] #treatedProfessionallyQuestion-Disagree
 *  - [ ] #treatedProfessionallyQuestion-StronglyDisagree
 *  During the compliance check, how easy was it for you to understand what was happening?
 *  - [ ] #complianceCheckUnderstandingQuestion-VeryEasy
 *  - [ ] #complianceCheckUnderstandingQuestion-Easy
 *  - [x] #complianceCheckUnderstandingQuestion-NeitherEasyOrDifficult
 *  - [ ] #complianceCheckUnderstandingQuestion-Difficult
 *  - [ ] #complianceCheckUnderstandingQuestion-VeryDifficult
 *  Why did you give this answer?
 *  - [x] #whyGiveAnswer
 *  - 'Write anything in here as long as it doesn’t go over 1,000 characters.'
 *  How confident are you that your interactions with HMRC during this compliance check will support you in meeting your future tax obligations?
 *  - [ ] #supportFutureTaxQuestion-VeryConfident
 *  - [ ] #supportFutureTaxQuestion-FairlyConfident
 *  - [x] #supportFutureTaxQuestion-Neutral
 *  - [ ] #supportFutureTaxQuestion-NotVeryConfident
 *  - [ ] #supportFutureTaxQuestion-NotAtAllConfident
 **/
module.exports = {
  id: 'feedback-frontend--ccg',
  scenarios: [
    {
      clickSelectors: [
        '#treatedProfessionallyQuestion-NeitherAgreeNorDisagree',
        '#complianceCheckUnderstandingQuestion-NeitherEasyOrDifficult',
        '#supportFutureTaxQuestion-Neutral',
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
          selector: '#whyGiveAnswer',
        },
      ],
      label: 'Feedback frontend: ccg',
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
      url: 'http://localhost:9514/feedback-survey/feedback/ccg',
    },
  ],
};
