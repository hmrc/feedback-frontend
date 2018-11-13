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

package navigation

import javax.inject.{Inject, Singleton}

import play.api.mvc.Call
import controllers.routes
import pages._
import models._

trait NextPage[A, B] {

  def nextPage(page: A): B => Call
}

object NextPage {

  implicit val otherQuestionsNextPage: NextPage[OtherQuestionsPage.type, UserAnswers] =
    new NextPage[OtherQuestionsPage.type, UserAnswers] {
      override def nextPage(page: OtherQuestionsPage.type): UserAnswers => Call = _ =>
        controllers.routes.ThankYouController.onPageLoad()
    }

  implicit val ptaQuestionsNextPage: NextPage[PTAQuestionsPage.type, UserAnswers] =
    new NextPage[PTAQuestionsPage.type, UserAnswers] {
      override def nextPage(page: PTAQuestionsPage.type): UserAnswers => Call = _ =>
        controllers.routes.ThankYouController.onPageLoad()
    }
}

@Singleton
class Navigator @Inject()() {

  def nextPage[A, B](page: A)(b: B)(implicit np: NextPage[A, B]): Call = np.nextPage(page)(b)
}
