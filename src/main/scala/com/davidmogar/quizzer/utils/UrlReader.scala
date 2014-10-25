package com.davidmogar.quizzer.utils

import java.io.{InputStreamReader, BufferedReader, IOException}
import java.net.URL

/**
 * Utility class ported from Java version of the application
 */
object UrlReader {

  /**
   * Gets the content of the URL as an String.
   *
   * @param url URL to fetch data from
   * @return string with the URL contents
   * @throws IOException if URL was invalid or the connection was refused
   */
  @throws(classOf[IOException])
  def getStreamAsString(url: URL): String = {
    val connection = url.openConnection
    val bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream))
    val response: StringBuilder = new StringBuilder

    var inputLine = bufferedReader.readLine()
    while (inputLine != null) {
      response.append(inputLine)
      inputLine = bufferedReader.readLine()
    }

    bufferedReader.close

    return response.toString
  }
}
