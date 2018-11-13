#!/bin/bash

echo "Applying migration PTAQuestions"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /pTAQuestions                       controllers.PTAQuestionsController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "pTAQuestions.title = pTAQuestions" >> ../conf/messages.en
echo "pTAQuestions.heading = pTAQuestions" >> ../conf/messages.en

echo "Migration PTAQuestions completed"
