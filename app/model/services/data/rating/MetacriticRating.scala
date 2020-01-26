package model.services.data.rating

import scala.util.Try

case class MetacriticRating(source: String, value: String) extends Rating {
  override val numericValue: Int = value.split("/").headOption.flatMap { v =>
    Try(v.toInt).toOption
  } getOrElse 0
}
