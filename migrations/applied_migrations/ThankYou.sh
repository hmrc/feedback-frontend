#!/bin/bash

echo "Applying migration ThankYou"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /thankYou                       controllers.ThankYouController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "thankYou.title = thankYou" >> ../conf/messages.en
echo "thankYou.heading = thankYou" >> ../conf/messages.en

echo "Migration ThankYou completed"
