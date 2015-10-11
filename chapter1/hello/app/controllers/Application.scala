package controllers

import play.api._
import play.api.mvc._

import play.api.cache._
import play.api.mvc._
import javax.inject.Inject

class Application @Inject() (cache: CacheApi) extends Controller {

  def index = Action {
    Ok("Hello world")
  }

  def hello(name: String) =Action {
        Ok(views.html.hello(name))
      }

}
