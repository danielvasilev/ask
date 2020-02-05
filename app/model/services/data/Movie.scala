package model.services.data

import akka.http.scaladsl.model.Uri
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsValue, Reads, __}

import model.services.data.rating.Rating

case class Movie(title: String, year: String, plot: String, country: String, ratings: Seq[Rating]) {
  def defaultRating: Int = ratings match {
    case Nil => 0
    case _ => ratings.map(_.numericValue).sum / ratings.size
  }

  val nextSearchLink: String = Uri("/search").withQuery(Uri.Query(Map(("SearchTerms", title)))).toString
}

object Movie {
  implicit val read: Reads[Movie] = {
    (
      (__ \ "Title").readNullable[String].map(_.getOrElse("")) and
        (__ \ "Year").readNullable[String].map(_.getOrElse("")) and
        (__ \ "Plot").readNullable[String].map(_.getOrElse("")) and
        (__ \ "Country").readNullable[String].map(_.getOrElse("")) and
        (__ \ "Ratings").read[List[Rating]]
      ) (Movie.apply _)
  }

  def fromJson: JsValue => Movie = { json => json.asOpt[Movie].getOrElse(EmptyMovie) }

  val EmptyMovie = Movie("", "", "", "", Nil)
}