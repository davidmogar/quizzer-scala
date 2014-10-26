package com.davidmogar.quizzer.serializers

import com.davidmogar.quizzer.domain.Grade
import play.api.libs.json._

import scala.collection.mutable

object AssessmentXmlSerializer {

   def serializeGrades(grades: mutable.HashMap[Long, Grade]): String = {
     var jsonString = "<scores>\n"

     for ((studentId, grade) <- grades) {
       jsonString += "\t<score>\n\t\t<studentId>" + studentId + "</studentId>\n"
       jsonString += "\t\t<value>" + grade.grade + "</value>\n\t</score>\n"
     }

     jsonString + "</scores>"
   }

   def serializeStatistics(statistics: mutable.HashMap[Long, Integer]): String = {
     var jsonString = "<statistics>\n"

     for ((questionId, value) <- statistics) {
       jsonString += "\t<item>\n\t\t<questionId>" + questionId + "</questionId>\n"
       jsonString += "\t\t<correctAnswers>" + value + "</correctAnswers>\n\t</item>\n"
     }

     jsonString + "</statistics>"
   }
 }
