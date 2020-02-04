package controllers

import javax.inject._

import play.api.libs.ws._
import play.api.mvc._

import model.search.SearchTerms
import model.services.data.Movie
import model.services.data.Movie.EmptyMovie
import model.services.{MovieDataService, ServiceRequestExecutor}

import scala.concurrent.ExecutionContext.global
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class SearchController @Inject()(val controllerComponents: ControllerComponents, ws: WSClient) extends BaseController with ControllerHelpers {
  def search(searchTerms: SearchTerms): Action[AnyContent] = Action.async { implicit request =>
    implicit val ex: ExecutionContext = global

    val movieDataService = MovieDataService(ws)
    val searchRequest = movieDataService.search(searchTerms)
    val serviceResponse = ServiceRequestExecutor[Movie](searchRequest).execute(Movie.fromJson)
    serviceResponse.flatMap {
      case EmptyMovie => Future.successful(Ok(views.html.index(s"[${searchTerms.toString}] is a dead end, try other search terms.")))
      case response: Movie => movieDataService.movieResult(response, movieDataService).map { movies =>
        Ok(views.html.searchPage(movies))
      }
    }
  }
}
