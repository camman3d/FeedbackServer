package models

import play.api.libs.json.{JsArray, Json}

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 10/14/13
 * Time: 1:31 PM
 * To change this template use File | Settings | File Templates.
 */
case class Feedback2(userId: String, featureResults: List[GradeResult2], restrictionResults: List[GradeResult2], comments: String)

object Feedback2 {
  def fromJson(text: String): Feedback2 = {
    val json = Json.parse(text)
    Feedback2(
      (json \ "userId").as[String],
      (json \ "featureResults").as[JsArray].value.toList.map(result => GradeResult2(
        (result \ "target" \ "name").as[String],
        (result \ "results").as[JsArray].value.toList.map(tcResult => TestCaseResult(
          (tcResult \ "name").as[String],
          (tcResult \ "notes").as[String]
        )),
        (result \ "score").as[Double],
        (result \ "target" \ "points").as[Double],
        (result \ "target" \ "extraCredit").as[Boolean],
        (result \ "notes").as[String]
      )),
      (json \ "restrictionResults").as[JsArray].value.toList.map(result => GradeResult2(
        (result \ "target" \ "name").as[String],
        (result \ "results").as[JsArray].value.toList.map(tcResult => TestCaseResult(
          (tcResult \ "name").as[String],
          (tcResult \ "notes").as[String]
        )),
        (result \ "score").as[Double],
        (result \ "target" \ "points").as[Double],
        extraCredit = false,
        (result \ "notes").as[String]
      )),
      (json \ "comments").as[String]
    )
  }
}
