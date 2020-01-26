package model.services.data.rating

import scala.util.Try

case class RottenTomatoesRating(source: String, value: String) extends Rating {
  override val numericValue: Int = Try(value.replaceAll("%", "").toInt).getOrElse(0)
}
