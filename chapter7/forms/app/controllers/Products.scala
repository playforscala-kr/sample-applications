package controllers

import javax.inject.Inject
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n._
import org.joda.time._
import scala.util.Try

import models._

class Products @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport{

  def eanExists(ean: Long) = true

  val productForm = Form(mapping(
  "ean" -> longNumber.verifying("This product already exists!",
    eanExists(_)),
  "name" -> nonEmptyText,
  "description" -> text,
  "pieces" -> number,
  "active" -> boolean)(Product.apply)(Product.unapply).verifying(
    "Product can not be active if the description is empty",
    product =>
      !product.active || product.description.nonEmpty))

  def createForm = Action {
    Ok(views.html.products.form(productForm))
  }

  def create() = Action { implicit request =>

    productForm.bindFromRequest.fold(
      formWithErrors => Ok(views.html.products.form(formWithErrors)),
      value => Ok("created: " + value)
    )
  }

  val tagsFormaaa = Form(mapping(
      "tags" -> list(text)
    )(Tags.apply)(Tags.unapply))

  def tagsForm() = Action { implicit request =>
    Ok(views.html.product.tags(tagsFormaaa))
  }
 
  def tags() = Action { implicit request =>
    tagsFormaaa.bindFromRequest.fold(
      formWithErrors => Ok(views.html.product.tags(formWithErrors)),
      value => Ok("created: " + value)
    )

  }


val contactMapping = tuple(
  "name" -> text,
  "email" -> email)
val contactsForm = Form(tuple(
  "main_contact" -> contactMapping,
  "technical_contact" -> contactMapping,
  "administrative_contact" -> contactMapping))


trait Formatter[T] {
  def bind(key: String, data: Map[String, String]):
    Either[Seq[FormError], T]
  def unbind(key: String, value: T): Map[String, String]
  val format: Option[(String, Seq[Any])] = None
}

implicit val localDateFormatter = new Formatter[LocalDate] {
  def bind(key: String, data: Map[String, String]) =
    data.get(key) map { value =>        // map으로 값을 얻음
      Try {
        Right(LocalDate.parse(value))   // String을 LocalDate으로 파싱
      } getOrElse Left(Seq(FormError(key, "error.date", Nil)))    // 파싱이 실패하면 오류 반환
    } getOrElse Left(Seq(FormError(key, "error.required", Nil)))    // 키가 발견되지 않으면 오류 반환
  def unbind(key: String, ld: LocalDate) = Map(key -> ld.toString)

  override val format = Some(("date.format", Nil))
}

}