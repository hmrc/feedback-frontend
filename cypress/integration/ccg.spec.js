/**
 * @name beta
 * @description Feedback
 * @link http://localhost:9514/feedback-survey/feedback/beta
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
 **/
const journey = 'feedback--ccg';
const whyGiveScore =
  'Overall, how did you feel about the service you received today?';

describe('Feedback :: ccg', () => {
  it('fills in the feedback form', () => {
    cy.visit('http://localhost:9514/feedback/PERTAX/ccg');

    describe('How strongly do you agree or disagree that HMRC treated you professionally during the compliance check?', () => {
      cy.get('#treatedProfessionallyQuestion-NeitherAgreeNorDisagree').click();
    });

    describe('During the compliance check, how easy was it for you to understand what was happening?', () => {
      cy.get('#complianceCheckUnderstandingQuestion-NeitherEasyOrDifficult').click();
    });

    describe('Why did you give this score?', () => {
      cy.get('#whyGiveScore')
        .type(whyGiveScore)
        .should('have.value', whyGiveScore);
    });

    describe('How confident are you that your interactions with HMRC during this compliance check will support you in meeting your future tax obligations?', () => {
      cy.get('#supportFutureTaxQuestion-Neutral').click();
    });

    describe('All done, submit the form', () => {
      cy.matchImageSnapshot(`${journey}`);
      cy.get('#submit').click();
    });

    describe('', () => {
      cy.matchImageSnapshot(`${journey}--thanks`);
    });
  });
});
