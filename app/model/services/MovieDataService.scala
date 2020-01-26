package model.services

import javax.inject.Inject

import akka.http.scaladsl.model.Uri
import play.api.libs.ws._

import model.search.SearchTerms

import scala.concurrent.ExecutionContext

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
}
