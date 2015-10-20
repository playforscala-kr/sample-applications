package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.cache._
import play.api.Play.current

class Application extends Controller {

  def index = Action {
  	Cache.set("product", "123213")
  	Cache.getAs[String]("product") match {
    	case Some(product) => Ok(product)
    	case None => Ok(views.html.index("Your new application is ready."))
  	}
  }

}
