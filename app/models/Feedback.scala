package models

import play.api.libs.json.{JsArray, Json}

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 9/30/13
 * Time: 10:59 AM
 * To change this template use File | Settings | File Templates.
 */
case class Feedback(userId: String, results: List[GradeResult], comments: String)

object Feedback {
  def fromJson(text: String): Feedback = {
    val json = Json.parse(text)
    Feedback(
      (json \ "userId").as[String],
      (json \ "results").as[JsArray].value.toList.map(result => GradeResult(
        (result \ "feature").as[String],
        (result \ "score").as[Double],
        (result \ "possible").as[Double],
        (result \ "extraCredit").as[Boolean],
        (result \ "notes").as[String]
      )),
      (json \ "comments").as[String]
    )
  }
}