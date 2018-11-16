#!/bin/bash

echo "Applying migration PTAQuestions"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /ptaQuestions                       controllers.PTAQuestionsController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "ptaQuestions.title = ptaQuestions" >> ../conf/messages.en
echo "ptaQuestions.heading = ptaQuestions" >> ../conf/messages.en

echo "Migration PTAQuestions completed"
