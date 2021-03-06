package com.davidmogar.quizzer.loaders

import java.net.URL

import com.davidmogar.quizzer.Assessment
import com.davidmogar.quizzer.deserializers.AssessmentDeserializer
import com.davidmogar.quizzer.utils.UrlReader

object AssessmentLoader {
  /**
   * Returns a new Assessment created with the data obtained from the URLs received as arguments.
   *
   * @param questionsUrl URL to the questions file
   * @param answersUrl   URL to the answers file
   * @param gradesUrl    URL to the grades file
   * @return an Assessment with the data from the URLs received as arguments or an String with the error occurred
   */
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

  /**
   * Returns a new Assessment created with the data obtained from the JSONs received as arguments.
   *
   * @param questionsJson JSON representation of the questions file
   * @param answersJson   JSON representation of the answers file
   * @param gradesJson    JSON representation of the grades file
   * @return an Assessment with the data from the JSONs received as arguments
   */
  def loadAssessmentFromJson(questionsJson: String, answersJson: String, gradesJson: Option[String] = None): Assessment = {
      if (gradesJson != None) {
        createAssessment(questionsJson, answersJson, gradesJson)
      } else {
        createAssessment(questionsJson, answersJson)
      }
  }

  /**
   * Creates and returns an Assessment with the data obtained after deserialize the given JSONs.
   *
   * @param questionsJson JSON representation of the questions file
   * @param answersJson   JSON representation of the answers file
   * @param gradesJson    JSON representation of the grades file
   * @return an Assessment with the data from the JSONs received as arguments
   */
  def createAssessment(questionsJson: String, answersJson: String, gradesJson: Option[String] = None): Assessment = {
    val questions = AssessmentDeserializer.deserializeQuestions(questionsJson)
    val answers = AssessmentDeserializer.deserializeAnswers(answersJson)

    if (gradesJson != None) {
      Assessment(questions, answers, AssessmentDeserializer.deserializeGrades(gradesJson.get))
    } else {
      Assessment(questions, answers)
    }
  }
}
