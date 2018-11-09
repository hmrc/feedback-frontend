package models

case class OtherQuestions(
                         ableToDo:          Option[Boolean],
                         howEasyScore:      Option[HowEasyQuestion],
                         whyGiveScore:      Option[String],
                         satisfactionScore: Option[HowDoYouFeelQuestion]
                         )