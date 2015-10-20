package controllers

import play.api._
import play.api.mvc._
import anorm.SQL
import anorm.SqlQuery
import play.api.Play.current
import play.api.db.DB
import models._
import play.api.libs.json._

class Application extends Controller {

  val sql: SqlQuery = SQL("select * from products order by name asc")

  import anorm.RowParser
  val productParser: RowParser[Product] = {
    import anorm.~
    import anorm.SqlParser._
    long("id") ~        // 필드 파서
    long("ean") ~
    str("name") ~
    str("description") map {
      case id ~ ean ~ name ~ description =>
        Product(id, ean, name, description)   // 우리가 패턴으로 설정하고 싶은 것
    }
  }

  import anorm.ResultSetParser
  val productsParser: ResultSetParser[List[Product]] = {                    
   productParser *
  }

  val stockItemParser: RowParser[StockItem] = {
    import anorm.SqlParser._
    import anorm.~
    long("id") ~ long("product_id") ~
        long("warehouse_id") ~ long("quantity") map {
      case id ~ productId ~ warehouseId ~ quantity =>
        StockItem(id, productId, warehouseId, quantity)
    }
  }

  def productStockItemParser: RowParser[(Product, StockItem)] = {
    import anorm.SqlParser._
    import anorm.~
    productParser ~ StockItem.stockItemParser map (flatten)                    
  }

  def getAllWithParser: List[Product] = DB.withConnection {
    implicit connection =>
    sql.as(productsParser)
  }


  def index = Action {
    Ok("Hello world")
  }

  def getAllProduct = Action {
    delete(Product(3L, 3333333L,"33333","33333"))
    Ok(Json.toJson(getAllWithParser))
  }

  def getAll: List[Product] = DB.withConnection {         // 코드를 실행하기 전에 커넥션을 생성하고 사용한 후에 종료
    implicit connection =>                // 커넥션을 implicit으로 선언
    sql().map ( row =>
      Product(row[Long]("id"), row[Long]("ean"),      // 각 열(Row)의 내용을 Product로 변환
        row[String]("name"), row[String]("description"))      // Stream은 게으르기(lazy) 때문에, 모든 결과를 조회할 수 있게 List로 변환
    ).toList
  }

  def getAllWithPatterns: List[Product] = DB.withConnection {
    implicit connection =>
    import anorm.Row
    
    sql().collect {       // 모든 열이 이 패턴에 매칭
      case Row(Some(id: Long), Some(ean: Long), Some(name: String), Some(description: String)) =>
        Product(id, ean, name, description) // 적합한 상품 생성
    }.toList
  }

  def getAllProductsWithStockItems: Map[Product, List[StockItem]] = {
    DB.withConnection { implicit connection =>
      val sql = SQL("select p.*, s.* " +      // 조인 쿼리
        "from products p " +
        "inner join stock_items s on (p.id = s.product_id)")
      val results: List[(Product, StockItem)] =
        sql.as(productStockItemParser *)          // ResultSet를 해석하는 RowParser 사용
      results.groupBy { _._1 }.mapValues { _.map { _._2 } } // 튜플 목록을 StockItem와 Products의 map으로 변환
    }
  }

  def insert(product: Product): Boolean =
      DB.withConnection { implicit connection =>
    val addedRows = SQL("""insert
    into products
    values ({id}, {ean}, {name}, {description})""").on(       // 중괄호로 둘러싸인 식별자는 on(...)의 요소와 맵핑 할 매개변수를 이름을 나타낸다
      "id" -> product.id,                                     // 매개변수는 이 값들과 맵핑된다
      "ean" -> product.ean,
      "name" -> product.name,
      "description" -> product.description
    ).executeUpdate()                     // executeUpdate는 영향이 있는 열의 수를 반환한다
    addedRows == 1
  }

  def update(product: Product): Boolean =
      DB.withConnection { implicit connection =>
    val updatedRows = SQL("""update products          // SQL update 구문
    set name = {name},
    ean = {ean},
    description = {description}
    where id = {id}
     """).on(
      "id" -> product.id,                 // 값과 매개변수 맵핑
      "name" -> product.name,
      "ean" -> product.ean,
      "description" -> product.description).
      executeUpdate()
    updatedRows == 1              // 갱신된 열과 예상된 열의 수가 일치하는지 확인
  }

  def delete(product: Product): Boolean =
      DB.withConnection { implicit connection =>
    val updatedRows = SQL("delete from products where id = {id}").
      on("id" -> product.id).executeUpdate()
    updatedRows == 0
  }
}
