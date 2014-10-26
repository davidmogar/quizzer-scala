package com.davidmogar.quizzer.deserializers

import com.davidmogar.quizzer.domain.Test
import play.api.libs.json.{Json, JsObject}


object TestsDeserializer {

  /**
   * Deserializes the JSON representation received as arguments to a list of Test objects.
   *
   * @param json JSON representation of Test objects
   * @return a list containing the tests deserialized
   */
  def deserialize(json: String): List[Test] = {
    var tests = List[Test]()
    val data = Json.parse(json)

    for (value <- (data \ "tests").as[List[JsObject]]) {
      val questionsUrl = (value \ "quizz").as[String]
      val answersUrl = (value \ "assessment").as[String]
      val gradesUrl = (value \ "scores").as[String]

      tests ::= Test(questionsUrl, answersUrl, gradesUrl)
    }

    return tests
  }
}
