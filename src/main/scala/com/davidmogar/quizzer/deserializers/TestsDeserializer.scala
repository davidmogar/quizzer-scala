package com.davidmogar.quizzer.deserializers

import com.davidmogar.quizzer.domain.Test
import play.api.libs.json.{Json, JsObject}


object TestsDeserializer {

  def deserialize(json: String): List[Test] = {
    var tests = List[Test]()
    val data = Json.parse(json)

    for (value <- (data \ "tests").as[List[JsObject]]) {
      var questionsUrl = (value \ "quizz").as[String]
      var answersUrl = (value \ "assessment").as[String]
      var gradesUrl = (value \ "scores").as[String]

      tests ::= Test(questionsUrl, answersUrl, gradesUrl)
    }

    return tests
  }
}
