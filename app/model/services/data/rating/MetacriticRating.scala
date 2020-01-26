package model.services.data.rating

case class MetacriticRating(source: String, value: String) extends Rating {
  override val numericValue: Double = ???
}
