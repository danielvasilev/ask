package model.services.data.rating

case class ImdbRating(source: String, value: String) extends Rating {
  override val numericValue: Double = ???
}
