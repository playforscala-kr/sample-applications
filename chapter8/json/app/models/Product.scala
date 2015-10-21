package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Product(ean: Long, name: String, description: String)

object Product {
  var products = Set( Product(5010255079763L, "Paperclips Large", "Large Plain Pack of 1000"),
    Product(5018206244666L, "Giant Paperclips", "Giant Plain 51mm 100 pack"),
    Product(5018306332812L, "Paperclip Giant Plain", "Giant Plain Pack of 10000"),
    Product(5018306312913L, "No Tear Paper Clip", "No Tear Extra Large Pack of 1000"),
    Product(5018206244611L, "Zebra Paperclips", "Zebra Length 28mm Assorted 150 Pack")
  )
  
  def findAll = this.products.toList.sortBy(_.ean)
  
  def findByEan(ean: Long) = this.products.find(_.ean == ean)
  
  def save(product: Product) = {			// 상품 저장, 기존 데이터 업데이트
    findByEan(product.ean).map( oldProduct =>
      this.products = this.products - oldProduct + product
    ).getOrElse(
      throw new IllegalArgumentException("Product not found")
    )
  }

  implicit object ProductWrites extends Writes[Product] {
    def writes(p: Product) = Json.obj(
      "ean" -> Json.toJson(p.ean),
      "name" -> Json.toJson(p.name),
      "description" -> Json.toJson(p.description)
    )
  }

  implicit val productWrites: Writes[Product] = (
      (JsPath \ "ean").write[Long] and
      (JsPath \ "name").write[String] and
      (JsPath \ "description").write[String]
    )(unlift(Product.unapply))

  implicit val productReads: Reads[Product] = (
    (JsPath \ "ean").read[Long] and
    (JsPath \ "name").read[String] and
    (JsPath \ "description").read[String]
  )(Product.apply _)
  
}
