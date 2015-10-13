package controllers

import play.api._
import play.api.mvc._
import java.util.Date
import models.PickList
import play.twirl.api.Html
import scala.concurrent.{ExecutionContext, Future}

class PickLists extends Controller {

  def preview(warehouse: String) = Action {
    val pickList = PickList.find(warehouse)
    val timestamp = new java.util.Date
    Ok(views.html.pickList(warehouse, pickList, timestamp))
  }

  def sendAsync(warehouse: String) = Action {
    import ExecutionContext.Implicits.global

    Future {
      val pickList = PickList.find(warehouse)
      send(views.html.pickList(warehouse, pickList, new Date))
    }
    Redirect(routes.PickLists.index())
  }

  /**
   * Renders the home page.
   */
  def index = Action {
    Ok(views.html.index())
  }

  /**
   * Stub for ‘sending’ the pick list as an HTML document, e.g. by e-mail.
   */
  private def send(html: Html) {
    Logger.info(html.body)
    // Send pick list…
  }

}
