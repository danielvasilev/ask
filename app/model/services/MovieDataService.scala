package model.services

import javax.inject.Inject

import akka.http.scaladsl.model.Uri
import play.api.libs.ws._

import model.Utils.StringUtils
import model.search.SearchTerms
import model.services.data.Movie
import model.services.data.Movie.EmptyMovie

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

case class MovieDataService @Inject()(ws: WSClient) {
  private val link = Uri("http://www.omdbapi.com")

  def search(searchTerms: SearchTerms)(implicit ec: ExecutionContext): WSRequest = {
    val query = Uri.Query(Map(
      ("t", searchTerms.value.mkString(" ")),
      ("apikey", "90eb0bb9")
    ))
    val url = link.withQuery(query)
    ws.url(url.toString)
  }

  def movieResult(responseMovie: Movie, movieDataService: MovieDataService)(implicit ec: ExecutionContext): Future[Seq[Movie]] = {
    val plotWords = responseMovie.plot.split(" ").filterNot(_.containsSpecialCharacters) :+ responseMovie.country
    val relatedSearchTerms = getRelatedSearchTerms(plotWords)
    val relatedMoviesFutures = relatedSearchTerms.map { relatedTerms =>
      val relatedSearchRequest = movieDataService.search(relatedTerms)
      ServiceRequestExecutor[Movie](relatedSearchRequest).execute(Movie.fromJson)
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
