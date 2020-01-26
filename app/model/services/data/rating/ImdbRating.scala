package model.services.data.rating

import scala.util.Try

case class ImdbRating(source: String, value: String) extends Rating {
  override val numericValue: Int = value.split("/").headOption.flatMap { v =>
    Try(v.toDouble).toOption
  }.fold(0) { v => (v * 10).toInt }
}
