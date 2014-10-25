package com.davidmogar.quizzer.loaders

import java.net.URL

import com.davidmogar.quizzer.deserializers.TestsDeserializer
import com.davidmogar.quizzer.domain.Test
import com.davidmogar.quizzer.utils.UrlReader

object TestsLoader {

  def loadTests(testsUrl: URL): Either[List[Test], String] = {
    try {
      Left(TestsDeserializer.deserialize(UrlReader.getStreamAsString(testsUrl)))
    } catch {
      case e: Exception => Right(e.getMessage)
    }
  }
}
