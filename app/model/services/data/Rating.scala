package model.services.data

import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, _}

case class Rating(source: String, value: String)

object Rating {
  implicit val read: Reads[Rating] = {
    (
      (__ \ "Source").readNullable[String].map(_.getOrElse("")) and
        (__ \ "Value").readNullable[String].map(_.getOrElse(""))) (Rating.apply _)
  }
  implicit val readList: Reads[List[Rating]] = Reads.list[Rating]
}