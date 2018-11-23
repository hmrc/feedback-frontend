/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package generators

import models._
import org.scalacheck.{Arbitrary, Gen}
import Arbitrary._
import Gen._

trait ModelGenerators {

  implicit lazy val arbitraryOtherQuestions: Arbitrary[OtherQuestions] = Arbitrary(otherQuestionsGen)

  implicit lazy val arbitraryPTAQuestions: Arbitrary[PTAQuestions] = Arbitrary(ptaQuestionsGen)

  implicit lazy val arbitraryBTAQuestions: Arbitrary[BTAQuestions] = Arbitrary(btaQuestionsGen)

  lazy val otherQuestionsGen: Gen[OtherQuestions] =
    for {
      ableToDo <- option(arbitrary[Boolean])
      howEasy  <- option(howEasyQuestionGen)
      whyScore <- option(arbitrary[String].suchThat(_.nonEmpty))
      howFeel  <- option(howDoYouFeelQuestionGen)
    } yield {
      OtherQuestions(ableToDo, howEasy, whyScore, howFeel)
    }

  lazy val ptaQuestionsGen: Gen[PTAQuestions] =
    for {
      neededToDo <- option(arbitrary[String].suchThat(_.nonEmpty))
      ableToDo   <- option(arbitrary[Boolean])
      howEasy    <- option(howEasyQuestionGen)
      whyScore   <- option(arbitrary[String].suchThat(_.nonEmpty))
      howFeel    <- option(howDoYouFeelQuestionGen)
    } yield {
      PTAQuestions(neededToDo, ableToDo, howEasy, whyScore, howFeel)
    }

  lazy val btaQuestionsGen: Gen[BTAQuestions] =
    for {
      mainService <- option(mainServiceQuestionGen)
      ableToDo    <- option(arbitrary[Boolean])
      howEasy     <- option(howEasyQuestionGen)
      whyScore    <- option(arbitrary[String].suchThat(_.nonEmpty))
      howFeel     <- option(howDoYouFeelQuestionGen)
    } yield {
      BTAQuestions(mainService, ableToDo, howEasy, whyScore, howFeel)
    }

  lazy val howEasyQuestionGen: Gen[HowEasyQuestion] =
    oneOf(HowEasyQuestion.values.toSeq)

  lazy val howDoYouFeelQuestionGen: Gen[HowDoYouFeelQuestion] =
    oneOf(HowDoYouFeelQuestion.values.toSeq)

  lazy val mainServiceQuestionGen: Gen[MainServiceQuestion] =
    oneOf(MainServiceQuestion.values.toSeq)
}
