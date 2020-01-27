package model.services.data

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsValue, Reads, __}

import model.services.data.rating.Rating

import scala.util.Try

class Movie(val title: String, val year: String, val plot: String, val country: String, val ratings: Seq[Rating]) {
  def defaultRating: Int = ratings match {
    case Nil => 0
    case _ => ratings.map(_.numericValue).sum / ratings.size
  }
}

object Movie {
  def apply(title: String, Year: String, plot: String, country: String, ratings: Seq[Rating]) = new Movie(title, Year, plot, country, ratings)

  implicit val read: Reads[Movie] = {
    (
      (__ \ "Title").readNullable[String].map(_.getOrElse("")) and
        (__ \ "Year").readNullable[String].map(_.getOrElse("")) and
        (__ \ "Plot").readNullable[String].map(_.getOrElse("")) and
        (__ \ "Country").readNullable[String].map(_.getOrElse("")) and
        (__ \ "Ratings").read[List[Rating]]
      ) (Movie.apply _)
  }

  def fromJson: JsValue => Movie = { json => Try(json.as[Movie]).getOrElse(EmptyMovie) }
}

object EmptyMovie extends Movie("", "", "", "", Nil)