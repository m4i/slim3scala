package org.slim3scala.gen.util

object SeqUtil {
  def toSentence(
      seq: Seq[String],
      twoWordsConnector: String = " and ",
      lastWordConnector: String = null,
      wordsConnector: String = ", "): String = {

    val _lastWordConnector =
      if (lastWordConnector == null) "," + twoWordsConnector
      else                           lastWordConnector

    seq.length match {
      case 0 => ""
      case 1 => seq.head
      case 2 => seq.mkString(twoWordsConnector)
      case _ =>
        seq.init.mkString(wordsConnector) + _lastWordConnector + seq.last
    }
  }
}
