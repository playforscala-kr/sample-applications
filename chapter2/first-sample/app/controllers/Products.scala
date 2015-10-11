package controllers

import play.api.mvc._
import play.api.mvc.Flash
import play.api.data.Form
import play.api.data.Forms.{mapping, longNumber, nonEmptyText}
import play.api.i18n._
import javax.inject.Inject
import models.Product

class Products @Inject() (val messagesApi: MessagesApi)
    extends Controller with I18nSupport {

  private val productForm: Form[Product] = Form(
    mapping(
      "ean" -> longNumber.verifying(		// 폼의 필드와 제약사항
        "validation.ean.duplicate", Product.findByEan(_).isEmpty),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)		// 폼과 모델 사이에 맵핑하는 함수
  )

  def list = Action { implicit request =>
      val products = Product.findAll
      Ok(views.html.products.list(products))
  }
  
  def show(ean: Long) = Action { implicit request =>
      Product.findByEan(ean).map { product =>
          Ok(views.html.products.details(product))        // 상품 상세사항을 렌더링 하거나
      }.getOrElse(NotFound)           // 404 페이지 반환
  }
  
def save = Action { implicit request =>
  val newProductForm = productForm.bindFromRequest()
  newProductForm.fold(
    hasErrors = { form =>
      Console println form
      Redirect(routes.Products.newProduct()).
        flashing(Flash(form.data) +   // 유용한 정보 메시지를 플래시 스코프에 폼 데이터로 추가함
        ("error" -> Messages("validation.errors")))
    },
    success = { newProduct =>
      Product.add(newProduct)
      val message = Messages("products.new.success", newProduct.name)
        Redirect(routes.Products.show(newProduct.ean)).
        flashing("success" -> message)  // 플래시 스코프에 확인 메시지를 추가함
    }
  )
}

  def newProduct = Action { implicit request =>
    val form = if (request2flash.get("error").isDefined)
        productForm.bind(request2flash.data)
      else
        productForm
    Ok(views.html.products.editProduct(form))
  }
}