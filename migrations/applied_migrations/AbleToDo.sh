#!/bin/bash

echo "Applying migration AbleToDo"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /ableToDo                        controllers.AbleToDoController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /ableToDo                        controllers.AbleToDoController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAbleToDo                  controllers.AbleToDoController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAbleToDo                  controllers.AbleToDoController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "ableToDo.title = ableToDo" >> ../conf/messages.en
echo "ableToDo.heading = ableToDo" >> ../conf/messages.en
echo "ableToDo.checkYourAnswersLabel = ableToDo" >> ../conf/messages.en
echo "ableToDo.error.required = Select yes if ableToDo" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAbleToDoUserAnswersEntry: Arbitrary[(AbleToDoPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AbleToDoPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAbleToDoPage: Arbitrary[AbleToDoPage.type] =";\
    print "    Arbitrary(AbleToDoPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AbleToDoPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def ableToDo: Option[AnswerRow] = userAnswers.get(AbleToDoPage) map {";\
     print "    x => AnswerRow(\"ableToDo.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AbleToDoController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration AbleToDo completed"
