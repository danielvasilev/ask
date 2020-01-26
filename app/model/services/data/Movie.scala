package model.services.data

import play.api.libs.json.{Reads, __}
import play.api.libs.functional.syntax._

case class Movie(name: String, Year: String, plot: String, ratings: Seq[Rating])

object Movie {
  implicit val read: Reads[Movie] = {
    (
      (__ \ "Name").readNullable[String].map(_.getOrElse("")) and
      (__ \ "Year").readNullable[String].map(_.getOrElse("")) and
      (__ \ "Plot").readNullable[String].map(_.getOrElse("")) and
        (__ \ "Ratings").read[List[Rating]]) (Movie.apply _)
  }
}
