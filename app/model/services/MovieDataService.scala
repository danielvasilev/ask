package model.services

import play.api.libs.ws._

import model.Utils.StringUtils
import model.search.SearchTerms
import model.services.data.{Movie, MovieQueryBuilder}
import model.services.data.Movie.EmptyMovie

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

case class MovieDataService(ws: WSClient) {
  def movieResult(responseMovie: Movie, movieDataService: MovieDataService)(implicit ec: ExecutionContext): Future[Seq[Movie]] = {
    val plotWords = responseMovie.plot.split(" ").filterNot(_.containsSpecialCharacters) :+ responseMovie.country
    val relatedSearchTerms = getRelatedSearchTerms(plotWords)
    val relatedMoviesFutures = relatedSearchTerms.map { searchTerms =>
      val query = MovieQueryBuilder.search(searchTerms)
      ServiceRequestExecutor[Movie](query, ws).execute(Movie.fromJson)
    }
    val moviesResult = Future.sequence(relatedMoviesFutures).map(_ :+ responseMovie)
    moviesResult.map { response =>
      response.filterNot(_ == EmptyMovie).distinctBy(_.title).sortBy(_.defaultRating)(Ordering.Int.reverse)
    }
  }

  @tailrec
  private def getRelatedSearchTerms(plotWords: Array[String], acc: List[SearchTerms] = Nil, searchTermsLimit: Int = 9, maxTries: Int = 99): Seq[SearchTerms] = {
    if (acc.length == searchTermsLimit || plotWords.isEmpty || maxTries == 0)
      acc
    else {
      def randomIndex = Random.between(0, plotWords.length)

      val randomNumberTerms = Random.between(1, 3)
      val searchTerms = Seq(plotWords(randomIndex), plotWords(randomIndex)).take(randomNumberTerms).sorted
      getRelatedSearchTerms(plotWords, (acc :+ SearchTerms(searchTerms)).distinct, maxTries = maxTries - 1)
    }
  }
}
