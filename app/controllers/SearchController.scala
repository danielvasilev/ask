package controllers

import javax.inject._

import play.api.libs.ws._
import play.api.mvc._

import model.search.SearchTerms
import model.services.data.Movie
import model.services.{MovieDataService, ServiceRequestExecutor}

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.global

@Singleton
class SearchController @Inject()(val controllerComponents: ControllerComponents, ws: WSClient) extends BaseController with ControllerHelpers {
  def search(searchTerms: SearchTerms): Action[AnyContent] = Action.async { implicit request =>
    implicit val ex: ExecutionContext = global

    val movieDataService = MovieDataService(ws)
    val searchRequest = movieDataService.search(searchTerms)
    val serviceRequestExecutor = ServiceRequestExecutor[Movie](searchRequest, Movie.fromJson).execute

    serviceRequestExecutor.map { response =>
      Ok(views.html.searchPage(response))
    }
  }
}
