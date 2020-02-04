package controllers

import javax.inject._

import play.api.libs.ws._
import play.api.mvc._

import model.Utils.StringUtils
import model.search.SearchTerms
import model.services.data.Movie
import model.services.data.Movie.EmptyMovie
import model.services.{MovieDataService, ServiceRequestExecutor}

import scala.concurrent.ExecutionContext.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random


@Singleton
class SearchController @Inject()(val controllerComponents: ControllerComponents, ws: WSClient) extends BaseController with ControllerHelpers {
  def search(searchTerms: SearchTerms): Action[AnyContent] = Action.async { implicit request =>
    implicit val ex: ExecutionContext = global

    val movieDataService = MovieDataService(ws)
    val searchRequest = movieDataService.search(searchTerms)
    val serviceResponse = ServiceRequestExecutor[Movie](searchRequest).execute(Movie.fromJson)
    serviceResponse.flatMap {
      case EmptyMovie => Future.successful(Ok(views.html.index(s"[${searchTerms.toString}] is a dead end, try other search terms.")))
      case response: Movie => movieResult(response, movieDataService)
    }
  }

  private def movieResult(responseMovie: Movie, movieDataService: MovieDataService)(implicit ec: ExecutionContext): Future[Result] = {
    val plotWords = responseMovie.plot.split(" ").filterNot(_.containsSpecialCharacters) :+ responseMovie.country
    val relatedSearchTerms = getRelatedSearchTerms(plotWords)
    val relatedMoviesFutures = relatedSearchTerms.map { relatedTerms =>
      val relatedSearchRequest = movieDataService.search(relatedTerms)
      ServiceRequestExecutor[Movie](relatedSearchRequest).execute(Movie.fromJson)
    }
    val moviesResult = Future.sequence(relatedMoviesFutures).map(_ :+ responseMovie)
    moviesResult.map { response =>
      val moviesToRender = response.filterNot(_ == EmptyMovie).distinctBy(_.title).sortBy(_.defaultRating)(Ordering.Int.reverse)
      Ok(views.html.searchPage(moviesToRender))
    }
  }

  private def getRelatedSearchTerms(plotWords: Array[String], acc: List[SearchTerms] = Nil, length: Int = 9, cycles: Int = 99): Seq[SearchTerms] = {
    if (acc.length == length || plotWords.isEmpty || cycles == 0)
      acc
    else {
      def randomIndex = Random.between(0, plotWords.length)

      val randomNumberTerms = Random.between(1, 3)
      val searchTerms = Seq(plotWords(randomIndex), plotWords(randomIndex)).take(randomNumberTerms).sorted
      getRelatedSearchTerms(plotWords, (acc :+ SearchTerms(searchTerms)).distinct, cycles = cycles - 1)
    }
  }
}
