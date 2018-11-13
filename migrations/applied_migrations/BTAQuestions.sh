#!/bin/bash

echo "Applying migration BTAQuestions"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /bTAQuestions                       controllers.BTAQuestionsController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "bTAQuestions.title = bTAQuestions" >> ../conf/messages.en
echo "bTAQuestions.heading = bTAQuestions" >> ../conf/messages.en

echo "Migration BTAQuestions completed"
