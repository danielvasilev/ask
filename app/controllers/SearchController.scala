package controllers

import javax.inject._

import play.api.libs.ws._
import play.api.mvc._

import model.search.SearchTerms
import model.services.MovieDataService

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.global

@Singleton
class SearchController @Inject()(val controllerComponents: ControllerComponents, ws: WSClient) extends BaseController with ControllerHelpers {
  def search(searchTerms: SearchTerms): Action[AnyContent] = Action.async { implicit request =>
    implicit val ex: ExecutionContext = global

    val movieDataService = MovieDataService(ws)
    val searchResults = movieDataService.search(searchTerms)

    searchResults.map { response =>
      Ok(views.html.searchPage(response.toString))
    }
  }
}
