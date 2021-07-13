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
const journey = 'feedback--trusts';
const whyNotAbleToDo =
  'I was able to do everything.';

  const whyGiveScore =
  'It has been a good experience.';

describe('Feedback :: Trusts', () => {
  it('fills in the feedback form', () => {
    cy.visit('http://localhost:9514/feedback-survey/trusts');

    describe('Are you an agent that is working on behalf of a client?', () => {
      cy.get('#isAgent-yes').click();
    });

    describe('What were you trying to do on the service today?', () => {
      cy.get('#TryingToDoQuestion-RegisterATrust').click();
    });

    describe('Were you able to do what you needed to do today?', () => {
      cy.get('#ableToDo-yes').click();
    });

    describe('If you were not able to do what you needed to do, tell us why.', () => {
      cy.get('#whyNotAbleToDo')
        .type(whyNotAbleToDo)
        .should('have.value', whyNotAbleToDo);
    });

    describe('How easy was it for you to do what you needed to do today?', () => {
      cy.get('#howEasyQuestion-Moderate').click();
    });

    describe('Why did you give this score?', () => {
      cy.get('#whyGiveScore')
        .type(whyGiveScore)
        .should('have.value', whyGiveScore);
    });

    describe('Overall, how did you feel about the service you received today?', () => {
      cy.get('#howDoYouFeelQuestion-Moderate').click();
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
