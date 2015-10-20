package controllers

import play.api._
import play.api.mvc._

class Application extends Controller with WithCart {

  def index = Action { implicit request =>
    val a = Ok(views.html.index())

    Console println a
    Ok(views.html.index())
  }

  def contact = Action { implicit request =>
    Ok(views.html.contact())
  }

  def home = Action { implicit request =>
  	Redirect(routes.Application.home())
  }

}