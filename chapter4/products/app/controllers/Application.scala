package controllers

import play.api.mvc.{Action, Controller}

class Application extends Controller {

  def home = Action {
    Ok(views.html.index())
  }
}