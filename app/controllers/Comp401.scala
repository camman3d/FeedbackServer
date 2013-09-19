package controllers

import play.api.mvc.{Result, Request, Action, Controller}
import play.api.libs.ws.WS
import concurrent.ExecutionContext.Implicits.global
import models.FeedbackFinder

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 9/19/13
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
object Comp401 extends Controller {

  def AuthenticatedAction(f: Request[_] => String => Result) = Action {
    request =>
      if (request.session.get("onyen").isDefined) {
        f(request)(request.session("onyen"))
      } else
        Redirect(routes.Comp401.index())
  }

  def index = Action {
    implicit request =>
    //    val url = routes.Comp401.index
      val url = routes.Comp401.index().absoluteURL()
      Ok(views.html.comp401.index(url))
  }

  def auth = Action(parse.urlFormEncoded) {
    implicit request =>
      val key = request.body("vfykey")(0)
      val onyen = request.body("onyen")(0)

      val url = "https://onyen.unc.edu/cgi-bin/unc_id/authenticator.pl/" + key

      Async {
        WS.url(url).get().map(response => {
          val lines = response.body.lines.toList
          if (lines(0) == onyen + ": pass")
            Redirect(routes.Comp401.home()).withSession("onyen" -> onyen)
          else
            Redirect(routes.Comp401.index())
        })
      }
  }

  def home = AuthenticatedAction {
    request =>
      onyen =>
        Ok(views.html.comp401.home(onyen, FeedbackFinder.countProjects))
  }

  def viewFeedback(id: Int) = AuthenticatedAction {
    request =>
      onyen =>
        val feedback = FeedbackFinder.getFeedback(onyen, id)
        if (feedback.isDefined) {
          if (feedback.get.isEmpty)
            Ok("Sorry, there is no feedback for you on this assignment. Please talk to a TA.")
          else
            Ok(feedback.get)
        } else
          Ok("Sorry, but there's no record of any grading done for you. Please talk to a TA.")
  }


}
