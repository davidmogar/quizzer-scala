package com.davidmogar.quizzer.loaders

import java.net.URL

import com.davidmogar.quizzer.Assessment
import com.davidmogar.quizzer.deserializers.AssessmentDeserializer
import com.davidmogar.quizzer.utils.UrlReader

object AssessmentLoader {

  def loadAssessmentFromUrl(questionsUrl: URL, answersUrl: URL, gradesUrl: Option[URL] = None): Either[Assessment, String] = {
    try {
      val questionsJson = UrlReader.getStreamAsString(questionsUrl)
      val answersJson = UrlReader.getStreamAsString(answersUrl)

      if (gradesUrl != None) {
        val gradesJson: String = UrlReader.getStreamAsString(gradesUrl.get)
        Left(createAssessment(questionsJson, answersJson, Option(gradesJson)))
      } else {
        Left(createAssessment(questionsJson, answersJson))
      }
    } catch {
      case e: Exception => Right(e.getMessage)
    }
  }

  def loadAssessmentFromJson(questionsJson: String, answersJson: String, gradesJson: Option[String] = None): Assessment = {
      if (gradesJson != None) {
        createAssessment(questionsJson, answersJson, gradesJson)
      } else {
        createAssessment(questionsJson, answersJson)
      }
  }

  def createAssessment(questionJson: String, answerJson: String, gradesJson: Option[String] = None): Assessment = {
    val questions = AssessmentDeserializer.deserializeQuestions(questionJson)
    val answers = AssessmentDeserializer.deserializeAnswers(answerJson)

    if (gradesJson != None) {
      Assessment(questions, answers, AssessmentDeserializer.deserializeGrades(gradesJson.get))
    } else {
      Assessment(questions, answers)
    }
  }
}
