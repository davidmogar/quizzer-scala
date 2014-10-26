package com.davidmogar.quizzer.loaders

import java.net.URL

import org.scalatest.{Matchers, BeforeAndAfter}

class AssessmentLoader$Test extends org.scalatest.FlatSpec with BeforeAndAfter with Matchers {

  "An AssessmentLoader" should "load all data" in {
    val questionsUrl = getClass.getResource("/questions.json")
    val answersUrl = getClass.getResource("/answers.json")
    val gradesUrl = getClass.getResource("/grades.json")

    questionsUrl should not be null
    answersUrl should not be null
    gradesUrl should not be null

    try {
      var assessment = AssessmentLoader.loadAssessmentFromUrl(questionsUrl, answersUrl, Option(gradesUrl))
      assessment should not be null
      assessment.isLeft should be (true)

      assessment = AssessmentLoader.loadAssessmentFromUrl(questionsUrl, answersUrl)
      assessment should not be null
      assessment.isLeft should be (true)
    } catch {
      case e: Exception => fail("Exception not expected")
    }
  }
}
