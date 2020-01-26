package model.services.data

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsValue, Reads, __}

import model.services.data.rating.Rating

import scala.util.Try

class Movie(val title: String, val Year: String, val plot: String, val ratings: Seq[Rating]) {
  def defaultRating: Int = ratings.map(_.numericValue).sum / ratings.size
}

object Movie {
  def apply(title: String, Year: String, plot: String, ratings: Seq[Rating]) = new Movie(title, Year, plot, ratings)

  implicit val read: Reads[Movie] = {
    (
      (__ \ "Title").readNullable[String].map(_.getOrElse("")) and
        (__ \ "Year").readNullable[String].map(_.getOrElse("")) and
        (__ \ "Plot").readNullable[String].map(_.getOrElse("")) and
        (__ \ "Ratings").read[List[Rating]]) (Movie.apply _)
  }

  def fromJson: JsValue => Movie = { json => Try(json.as[Movie]).getOrElse(empty) }

  val empty = EmptyMovie()
}

case class EmptyMovie() extends Movie("", "", "", Nil)