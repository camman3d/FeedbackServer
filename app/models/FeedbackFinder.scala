package models

import play.api.cache.Cache
import play.api.Play.current
import java.io.File
import org.apache.commons.io.FileUtils
import play.api.libs.json.{JsUndefined, Json}

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 9/19/13
 * Time: 12:23 PM
 * To change this template use File | Settings | File Templates.
 */
object FeedbackFinder {

  def countProjects: Int = {
    val projectCount = Cache.getAs[Int]("projectCount")
    if (projectCount.isEmpty) {
      // Cache has expired. Count again
      val dataFolder = new File("data")
      val count = dataFolder.listFiles().count(_.getName.startsWith("Assignment"))

      // Save the count and expire it in 10 mins
      Cache.set("projectCount", count, 600)
      count
    } else
      projectCount.get
  }

  def getFeedback(onyen: String, project: Int): Option[Either[String, Either[Feedback, Feedback2]]] = {
    val projectFiles = new File("data/Assignment" + project).listFiles()
    projectFiles.find(_.getName.contains("(" + onyen + ")")).map(file => {
      val contents = FileUtils.readFileToString(file)
      if (file.getName.endsWith(".json")) {
        val json = Json.parse(contents)
        if ((json \ "featureResults").isInstanceOf[JsUndefined])
          Right(Left(Feedback.fromJson(contents)))
        else
          Right(Right(Feedback2.fromJson(contents)))
      } else
        Left(contents)
    })
  }
}
