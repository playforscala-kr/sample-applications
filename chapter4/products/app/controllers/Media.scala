package controllers

import play.api.mvc.{Action, Controller}

class Media extends Controller {

    def photo(file: String) = Action {
      Ok("file: " + file)
    }
}