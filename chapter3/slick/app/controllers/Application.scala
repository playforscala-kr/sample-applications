package controllers

import scala.util.{Success, Failure}
import play.api._
import play.api.mvc._
import play.api.db.DB
import play.api.Play.current
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global


class Application extends Controller {

  // Product table definition
  class Product(tag: Tag) extends Table[(Long, String, String)](tag, "PRODUCTS") {
    def ean = column[Long]("EAN", O.PrimaryKey)
    def name = column[String]("NAME")
    def description = column[String]("DESCRIPTION")
    def * = (ean, name, description)
  }

  def index = Action {

    val products = TableQuery[Product]

    def db: Database = Database.forDataSource(DB.getDataSource())

    db.run(products.result) onComplete {
        case Success(posts) => for (post <- posts) println(post)
        case Failure(t) => println("An error has occured: " + t.getMessage)
    }

    Ok("Slick Example. Please check your console.")
  }

}
