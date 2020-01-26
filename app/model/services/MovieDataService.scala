package model.services

import javax.inject.Inject

import akka.http.scaladsl.model.Uri
import play.api.libs.json.JsValue
import play.api.libs.ws._

import model.search.SearchTerms

import scala.concurrent.{ExecutionContext, Future}

case class MovieDataService @Inject()(ws: WSClient) {
  private val link = Uri("http://www.omdbapi.com")

  def search(searchTerms: SearchTerms)(implicit ec: ExecutionContext): Future[JsValue] = {
    val query = Uri.Query(Map(
      ("t", searchTerms.value.mkString(" ")),
      ("apikey", "90eb0bb9")
    ))
    val url = link.withQuery(query)
    val request: WSRequest = ws.url(url.toString)

    request.get().map(_.json)
  }
}
