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
const journey = 'feedback--give-reason';

describe('Feedback :: Give-reason', () => {
  it('fills in the feedback form', () => {
    cy.visit('http://localhost:9514/feedback/PERTAX/give-reason');

    describe('What did you come to do?', () => {
      cy.get('#giveReason-checkTaxYear').click();
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
