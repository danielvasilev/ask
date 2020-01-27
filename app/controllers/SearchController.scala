package controllers

import javax.inject._

import play.api.libs.ws._
import play.api.mvc._

import model.search.SearchTerms
import model.services.data.{EmptyMovie, Movie}
import model.services.{MovieDataService, ServiceRequestExecutor}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.global
import model.Utils.StringUtils

import scala.util.Random


@Singleton
class SearchController @Inject()(val controllerComponents: ControllerComponents, ws: WSClient) extends BaseController with ControllerHelpers {
  def search(searchTerms: SearchTerms): Action[AnyContent] = Action.async { implicit request =>
    implicit val ex: ExecutionContext = global

    val movieDataService = MovieDataService(ws)
    val searchRequest = movieDataService.search(searchTerms)
    val serviceRequestExecutor = ServiceRequestExecutor[Movie](searchRequest).execute(Movie.fromJson)
    serviceRequestExecutor.flatMap {
      case EmptyMovie => Future.successful(Ok(views.html.index(s"Nor results for [${searchTerms.toString}], try other search terms.")))
      case response: Movie =>
        val plotWords = response.plot.split(" ").filterNot(_.containsSpecialCharacters)
        val relatedSearchTerms = getRelatedSearchTerms(plotWords, Nil)
        val relatedMoviesFutures = relatedSearchTerms.map { relatedTerms =>
          val relatedSearchRequest = movieDataService.search(relatedTerms)
          val relatedMoviesRequestExecutor = ServiceRequestExecutor[Movie](relatedSearchRequest).execute(Movie.fromJson)
          relatedMoviesRequestExecutor
        }
        val moviesResult = Future.sequence(relatedMoviesFutures).map(_ :+ response)
        moviesResult.map { response =>
          Ok(views.html.searchPage(response.sortBy(_.defaultRating).reverse))
        }
    }
  }

  private def getRelatedSearchTerms(plotWords: Array[String], acc: List[SearchTerms], length: Int = 9): Seq[SearchTerms] = {
    if (acc.length == length || plotWords.isEmpty)
      acc
    else {
      val randomIndex = Random.between(0, plotWords.length - 1)
      val searchTerms = plotWords(randomIndex)
      getRelatedSearchTerms(plotWords, (acc :+ SearchTerms(searchTerms)).distinct)
    }
  }
}
