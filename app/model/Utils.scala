package model

object Utils {
  implicit class StringUtils(val string: String) extends AnyVal {
    def cleanseSearchTerms: String = {
      string.toLowerCase.toArray.map(char =>
        if (!char.isLetterOrDigit || !char.isSpaceChar == ' ') ' ' else char).mkString.replaceAll("[ ]+", " ")
        .stripSuffix(" ")
    }

    def containsSpecialCharacters: Boolean = string.toLowerCase.exists(char => !char.isLetterOrDigit && !char.isSpaceChar)
  }
}
