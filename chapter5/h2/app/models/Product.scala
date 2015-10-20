package models

import play.api.libs.json._

case class Product(id: Long, ean: Long, name : String, description : String)

object Product{
	implicit val productWrites = Json.writes[Product]
}