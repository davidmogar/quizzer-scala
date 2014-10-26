package com.davidmogar.quizzer.serializers

import com.davidmogar.quizzer.domain.Grade

import scala.collection.mutable

object AssessmentSerializer {

  def serializeGrades(grades: mutable.HashMap[Long, Grade], format: String): String = {
    if (format == "xml") {
      AssessmentXmlSerializer.serializeGrades(grades)
    } else {
      AssessmentJsonSerializer.serializeGrades(grades)
    }
  }

  def serializeStatistics(statistics: mutable.HashMap[Long, Integer], format: String): String = {
    if (format == "xml") {
      AssessmentXmlSerializer.serializeStatistics(statistics)
    } else {
      AssessmentJsonSerializer.serializeStatistics(statistics)
    }
  }
}
