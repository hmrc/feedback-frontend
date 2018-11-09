package models

import play.api.libs.json._
import viewmodels.RadioOption

sealed trait HowDoYouFeelQuestion

object HowDoYouFeelQuestion {

  case object VerySatisfied    extends WithName("VerySatisfied") with HowDoYouFeelQuestion
  case object Satisfied        extends WithName("Satisfied") with HowDoYouFeelQuestion
  case object Moderate         extends WithName("Moderate") with HowDoYouFeelQuestion
  case object Dissatisfied     extends WithName("Dissatisfied") with HowDoYouFeelQuestion
  case object VeryDissatisfied extends WithName("VeryDissatisfied") with HowDoYouFeelQuestion

  val values: Set[HowDoYouFeelQuestion] =
    Set(VerySatisfied, Satisfied, Moderate, Dissatisfied, VeryDissatisfied)

  val options: Set[RadioOption] = values.map {
    value =>
      RadioOption("howDoYouFeelQuestion", value.toString)
  }

  implicit val enumerable: Enumerable[HowDoYouFeelQuestion] =
    Enumerable(values.toSeq.map(v => v.toString -> v): _*)

  implicit object HowDoYouFeelQuestionWrites extends Writes[HowDoYouFeelQuestion] {
    def writes(howDoYouFeelQuestion: HowDoYouFeelQuestion) = Json.toJson(howDoYouFeelQuestion.toString)
  }

  implicit object HowDoYouFeelQuestionReads extends Reads[HowDoYouFeelQuestion] {
    override def reads(json: JsValue): JsResult[HowDoYouFeelQuestion] = json match {
      case JsString(VerySatisfied.toString)    => JsSuccess(VerySatisfied)
      case JsString(Satisfied.toString)        => JsSuccess(Satisfied)
      case JsString(Moderate.toString)         => JsSuccess(Moderate)
      case JsString(Dissatisfied.toString)     => JsSuccess(Dissatisfied)
      case JsString(VeryDissatisfied.toString) => JsSuccess(VeryDissatisfied)
      case _                                   => JsError("Unknown howDoYouFeelQuestion")
    }
  }
}

