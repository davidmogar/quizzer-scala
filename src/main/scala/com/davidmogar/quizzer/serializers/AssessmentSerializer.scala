package com.davidmogar.quizzer.serializers

import com.davidmogar.quizzer.domain.Grade

import scala.collection.mutable

object AssessmentSerializer {
  /**
   * Returns an string with the representation of the grades in the desired format.
   *
   * @param grades grades to serialize
   * @param format format of the output
   * @return an string with the representation in the desired format
   */
  def serializeGrades(grades: mutable.HashMap[Long, Grade], format: String): String = {
    if (format == "xml") {
      AssessmentXmlSerializer.serializeGrades(grades)
    } else {
      AssessmentJsonSerializer.serializeGrades(grades)
    }
  }

  /**
   * Returns an string with the representation of the statistics in the desired format.
   *
   * @param statistics statistics to serialize
   * @param format     format of the output
   * @return an string with the representation in the desired format
   */
  def serializeStatistics(statistics: mutable.HashMap[Long, Integer], format: String): String = {
    if (format == "xml") {
      AssessmentXmlSerializer.serializeStatistics(statistics)
    } else {
      AssessmentJsonSerializer.serializeStatistics(statistics)
    }
  }
}
