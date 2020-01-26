package model.search

import play.api.mvc.QueryStringBindable
import model.Utils.StringUtils

case class SearchTerms(value: Seq[String]) extends AnyVal

object SearchTerms {
  def apply(string: String): SearchTerms = {
    SearchTerms(string.cleanseSearchTerms.split(" ").toList)
  }

  implicit val queryStringBinder = new QueryStringBindable[SearchTerms] {
    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, SearchTerms]] = {
      params.get(key).flatMap(_.headOption).map(value => Right(SearchTerms(value)))
    }

    override def unbind(key: String, value: SearchTerms): String = value.value.mkString(" ")
  }
}
