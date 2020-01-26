package model.services.data.rating

import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, _}

trait Rating {
  val source: String
  val value: String
  val numericValue: Double
}

object Rating {
  def apply(source: String, value: String): Rating = source match {
    case s if s.startsWith("Internet") => ImdbRating(source, value)
    case s if s.startsWith("Rotten") => RottenTomatoesRating(source, value)
    case _ => MetacriticRating(source, value)
  }

  implicit val readList: Reads[List[Rating]] = Reads.list[Rating] {
    (
      (__ \ "Source").readNullable[String].map(_.getOrElse("")) and
        (__ \ "Value").readNullable[String].map(_.getOrElse(""))) (Rating.apply _)
  }
}