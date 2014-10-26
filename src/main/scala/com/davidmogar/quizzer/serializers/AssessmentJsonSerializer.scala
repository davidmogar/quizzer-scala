package com.davidmogar.quizzer.serializers

import com.davidmogar.quizzer.domain.Grade
import play.api.libs.json._

import scala.collection.mutable

object AssessmentJsonSerializer {
  /**
   * Returns an string with the representation of the grades in JSON format.
   *
   * @param grades grades to serialize
   * @return an string with the representation in the desired format
   */
  def serializeGrades(grades: mutable.HashMap[Long, Grade]): String = {
    var jsonString = "{ \"scores\": ["

    for ((studentId, grade) <- grades) {
      jsonString += "{ \"studentId\": " + studentId + ", \"value\": " + grade.grade + " },"
    }
    if (jsonString.endsWith(",")) jsonString = jsonString.dropRight(1)

    jsonString += "] }"

    Json.prettyPrint(Json.parse(jsonString))
  }

  /**
   * Returns an string with the representation of the statistics in JSON format.
   *
   * @param statistics statistics to serialize
   * @return an string with the representation in the desired format
   */
  def serializeStatistics(statistics: mutable.HashMap[Long, Integer]): String = {
    var jsonString = "{ \"items\": ["

    for ((questionId, value) <- statistics) {
      jsonString += "{ \"questionId\": " + questionId + ", \"correctAnswers\": " + value + " },"
    }
    if (jsonString.endsWith(",")) jsonString = jsonString.dropRight(1)

    jsonString += "] }"

    Json.prettyPrint(Json.parse(jsonString))
  }
}
