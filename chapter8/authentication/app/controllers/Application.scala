package controllers

import play.mvc.Http
import play.api._
import play.api.mvc._
import scala.Left
import scala.Right
import play.api.http.HeaderNames

class Application extends Controller {

  def authenticate(request: Request[AnyContent]) = false

  def AuthenticatedAction(f: Request[AnyContent] => Result): Action[AnyContent] = {
    Action { request =>
      if (authenticate(request)) {
        f(request)
      } else {
        Unauthorized
      }
    }
  }

  def authenticate2(user: String, password: String) = {
    true
  }

  def AuthenticatedAction2(f: Request[AnyContent] => Result): Action[AnyContent] = {
    Action {
      request =>
        val maybeCredentials = readQueryString(request) orElse readBasicAuthentication(request.headers)
        maybeCredentials.map { resultOrCredentials =>
          resultOrCredentials match {					// 신원확인을 위해 패턴 매칭 사용
            case Left(errorResult) => errorResult		// 신원 확인 중 오류 발생
            case Right(credentials) => {
              val (user, password) = credentials
              if (authenticate2(user, password)) {		// 신원을 사용하여 인증 수행
                f(request)
              } else {
                Unauthorized("Invalid user name or password")
              }
            }
          }
        }.getOrElse {									// 신원정보를 읽을 수 없음
          val authenticate = (HeaderNames.WWW_AUTHENTICATE, "Basic")
          Unauthorized.withHeaders(authenticate)
        }
    }
  }

def readBasicAuthentication(headers: Headers): Option[Either[Result, (String, String)]] = {

  headers.get(Http.HeaderNames.AUTHORIZATION).map { header =>
    val BasicHeader = "Basic (.*)".r
    header match {
      case BasicHeader(base64) => {
        try {
          import org.apache.commons.codec.binary.Base64
          val decodedBytes = Base64.decodeBase64(base64.getBytes)
          val credentials = new String(decodedBytes).split(":", 2)
          if (credentials.length != 2) {
            Left(BadRequest("Invalid basic authentication"))
          } else {
            val (user, password) = (credentials(0), credentials(1))
            Right((user, password))
          }
        }
      }
      case _ => Left(BadRequest("Invalid Authorization header"))
    }
  }
}

  def index = AuthenticatedAction2 { request =>
    Ok("Authenticated response...")
  }

  def action = Action { request =>
    Ok("Response...")
  }

  def readQueryString(request: Request[_]): Option[Either[Result, (String, String)]] = {	// 오류나 인증확인을 위한 옵션 반환
    request.queryString.get("user").map { user =>
      request.queryString.get("password").map { password =>
        Right((user.head, password.head))		// 사용자 이름과 패스워드 쌍의 인증확인 반환
      }.getOrElse {
        Left(BadRequest("Password not specified"))		// HTTP 오류 반환
      }
    }
  }

}
