package controllers

import play.api.mvc.{Action, Controller}
import models._
import play.api.libs.json.Json
import play.api.libs.json._

class Products extends Controller {
  def list =Action {
    val productCodes = Product.findAll.map(_.ean)		// 상품 목록에서 EAN 코드를 추출
    Ok(Json.toJson(productCodes))		// JSON형식으로 변환
  }

  def details(ean: Long) = Action { 
    Product.findByEan(ean).map { product =>   // 매개변수로 전달된 EAN을 통해 상품을 찾는다)
      Ok(Json.toJson(product)(Product.productWrites))          // // JSON형식으로 상품을 출력한다 (아직 동작하지 않음)
    }.getOrElse(NotFound)
  }

  def save(ean: Long) = Action(parse.json) { request =>
    try {
      val productJson = request.body
      val product = productJson.as[Product]
      Product.save(product)
      Ok("Saved")
    }
    catch {
      case e:IllegalArgumentException => BadRequest("Product not found")
      case e:Exception => {
        BadRequest("Invalid EAN")
      }
    }
  }

}