package controllers

import play.api._
import play.api.mvc._
import models._

class Shop extends Controller with WithCart {

  def catalog() = Action { implicit request =>
    val product = Product.list
    Ok(views.html.products.catalog(product))
  }

}
