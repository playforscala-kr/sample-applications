package controllers

import play.api.mvc.{Action, Controller}
import concurrent.{ExecutionContext, Future}

class Dashboard extends Controller {
    
  def backlog(warehouse: String) = Action.async {
    import ExecutionContext.Implicits.global
    val backlog = scala.concurrent.Future {
      models.Order.backlog(warehouse)
    }
    backlog.map(value => Ok(value))
  }
}