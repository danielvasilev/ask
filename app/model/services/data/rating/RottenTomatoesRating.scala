package model.services.data.rating

case class RottenTomatoesRating(source: String, value: String) extends Rating {
  override val numericValue: Double = ???
}
