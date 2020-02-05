package model.services.data

import akka.http.scaladsl.model.Uri

import model.search.SearchTerms

import scala.concurrent.ExecutionContext

object MovieQueryBuilder {
  def search(searchTerms: SearchTerms)(implicit ec: ExecutionContext): Uri = {
    val query = Uri.Query(Map(
      ("t", searchTerms.value.mkString(" ")),
      ("apikey", "90eb0bb9")
    ))
    link.withQuery(query)
  }

  private val link = Uri("http://www.omdbapi.com")
}
