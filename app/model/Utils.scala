package model

object Utils {
  implicit class StringUtils(val string: String) extends AnyVal {
    def cleanseSearchTerms: String = {
      string.toLowerCase.filter(char => char.isLetterOrDigit || char.isSpaceChar).stripSuffix(" ")
    }

    def containsSpecialCharacters: Boolean = string.toLowerCase.exists(char => !char.isLetterOrDigit && !char.isSpaceChar)
  }
}
