#!/bin/bash

echo "Applying migration GiveReason"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /giveReason               controllers.GiveReasonController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /giveReason               controllers.GiveReasonController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeGiveReason                  controllers.GiveReasonController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeGiveReason                  controllers.GiveReasonController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "giveReason.title = Give feedback" >> ../conf/messages.en
echo "giveReason.heading = Give feedback" >> ../conf/messages.en
echo "giveReason.checkTaxCode = Check my tax code" >> ../conf/messages.en
echo "giveReason.checkTaxYear = Check my tax for this year" >> ../conf/messages.en
echo "giveReason.checkYourAnswersLabel = Give feedback" >> ../conf/messages.en
echo "giveReason.error.required = Select giveReason" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGiveReasonUserAnswersEntry: Arbitrary[(GiveReasonPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GiveReasonPage.type]";\
    print "        value <- arbitrary[GiveReason].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGiveReasonPage: Arbitrary[GiveReasonPage.type] =";\
    print "    Arbitrary(GiveReasonPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGiveReason: Arbitrary[GiveReason] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(GiveReason.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GiveReasonPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def giveReason: Option[AnswerRow] = userAnswers.get(GiveReasonPage) map {";\
     print "    x => AnswerRow(\"giveReason.checkYourAnswersLabel\", s\"giveReason.$x\", true, routes.GiveReasonController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration GiveReason completed"
