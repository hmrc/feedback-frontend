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
const journey = 'feedback--ccg-nmw';
const whyGiveAnswer =
  'It has been a good experience.';

describe('Feedback :: ccg-nmw', () => {
  it('fills in the feedback form', () => {
    cy.visit('http://localhost:9514/feedback/PERTAX/ccg');

    describe('How strongly do you agree or disagree that HMRC treated you professionally during the National Minimum Wage (NMW) enquiry?', () => {
      cy.get('#treatedProfessionallyQuestion-NeitherAgreeNorDisagree').click();
    });

    describe('During the NMW enquiry, how easy was it for you to understand what was happening?', () => {
      cy.get('#checkUnderstandingQuestion-NeitherEasyOrDifficult').click();
    });

    describe('Why did you give this answer?', () => {
      cy.get('#whyGiveAnswer')
        .type(whyGiveAnswer)
        .should('have.value', whyGiveAnswer);
    });

    describe('How confident are you, based on this enquiry, that HMRC will support you in meeting your future National Minimum Wage obligations?', () => {
      cy.get('#supportFutureQuestion-Neutral').click();
    });

    describe('All done, submit the form', () => {
      cy.matchImageSnapshot(`${journey}`);
      cy.get('#submit').click();
    });

    describe('take screenshot', () => {
      cy.matchImageSnapshot(`${journey}--thanks`);
    });
  });
});
