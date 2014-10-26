package com.davidmogar.quizzer.loaders

import java.net.URL

import com.davidmogar.quizzer.deserializers.TestsDeserializer
import com.davidmogar.quizzer.domain.Test
import com.davidmogar.quizzer.utils.UrlReader

object TestsLoader {

  /**
   * Returns a list of tests objects loaded from the file referenced by the URL argument.
   *
   * @param testsUrl URL to the tests file
   * @return a list of tests objects or an string with the error occurred.
   */
  def loadTests(testsUrl: URL): Either[List[Test], String] = {
    try {
      Left(TestsDeserializer.deserialize(UrlReader.getStreamAsString(testsUrl)))
    } catch {
      case e: Exception => Right(e.getMessage)
    }
  }
}
