package models

import play.api.cache.Cache
import play.api.Play.current
import java.io.File
import org.apache.commons.io.FileUtils

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

  def getFeedback(onyen: String, project: Int): Option[Either[String, Feedback]] = {
    val projectFiles = new File("data/Assignment" + project).listFiles()
    projectFiles.find(_.getName.contains("(" + onyen + ")")).map(file => {
      val contents = FileUtils.readFileToString(file)
      if (file.getName.endsWith(".json"))
        Right(Feedback.fromJson(contents))
      else
        Left(contents)
    })
  }
}
