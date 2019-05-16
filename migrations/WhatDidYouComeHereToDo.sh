#!/bin/bash

echo "Applying migration WhatDidYouComeHereToDo"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatDidYouComeHereToDo                        controllers.WhatDidYouComeHereToDoController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatDidYouComeHereToDo                        controllers.WhatDidYouComeHereToDoController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatDidYouComeHereToDo                  controllers.WhatDidYouComeHereToDoController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatDidYouComeHereToDo                  controllers.WhatDidYouComeHereToDoController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatDidYouComeHereToDo.title = whatDidYouComeHereToDo" >> ../conf/messages.en
echo "whatDidYouComeHereToDo.heading = whatDidYouComeHereToDo" >> ../conf/messages.en
echo "whatDidYouComeHereToDo.checkYourAnswersLabel = whatDidYouComeHereToDo" >> ../conf/messages.en
echo "whatDidYouComeHereToDo.error.required = Enter whatDidYouComeHereToDo" >> ../conf/messages.en
echo "whatDidYouComeHereToDo.error.length = WhatDidYouComeHereToDo must be 1000 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatDidYouComeHereToDoUserAnswersEntry: Arbitrary[(WhatDidYouComeHereToDoPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatDidYouComeHereToDoPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatDidYouComeHereToDoPage: Arbitrary[WhatDidYouComeHereToDoPage.type] =";\
    print "    Arbitrary(WhatDidYouComeHereToDoPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatDidYouComeHereToDoPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def whatDidYouComeHereToDo: Option[AnswerRow] = userAnswers.get(WhatDidYouComeHereToDoPage) map {";\
     print "    x => AnswerRow(\"whatDidYouComeHereToDo.checkYourAnswersLabel\", s\"$x\", false, routes.WhatDidYouComeHereToDoController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration WhatDidYouComeHereToDo completed"
