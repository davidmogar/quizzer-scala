package com.davidmogar.quizzer

import com.davidmogar.quizzer.domain._
import org.scalatest._

import scala.collection.mutable.HashMap

class AssessmentTest extends org.scalatest.FlatSpec with BeforeAndAfter with Matchers {

  var assessment: Assessment = _

  before {
    val questions = new HashMap[Long, Question]()
    val answers = new HashMap[Long, List[Answer]]()

    val multichoiceQuestion = MultichoiceQuestion(1, "Question 1")
    multichoiceQuestion.addAlternative(1, "Alternative 1", 0)
    multichoiceQuestion.addAlternative(2, "Alternative 2", 0.75)
    questions.put(1, multichoiceQuestion)

    questions.put(2, NumericalQuestion(2, "Question 2", 4.3, 1, -0.5))
    questions.put(3, TrueFalseQuestion(3, "Question 3", true, 1, -0.25, ""))

    answers.put(1, List[Answer](Answer(1, "2"), Answer(2, "4.3"), Answer(3, "true")))
    answers.put(2, List[Answer](Answer(1, "1"), Answer(2, "2"), Answer(3, "false")))
    answers.put(3, List[Answer](Answer(1, "2"), Answer(2, "2"), Answer(3, "true")))

    assessment = Assessment(questions, answers)
  }

  "An assessment" should "calculate grades correctly" in {
    assessment.calculateGrades()

    assessment.grades should have size 3
    assessment.grades.get(1).get.grade should be (2.75 +- 0.05)
    assessment.grades.get(2).get.grade should be (-0.75 +- 0.05)
  }

  it should "calculate each student grades correctly" in {
    assessment.calculateStudentGrade(1) should be (2.75 +- 0.05)
    assessment.calculateStudentGrade(2) should be (-0.75 +- 0.05)
  }

  it should "generate valid statistics" in {
    val statistics = assessment.getStatistics()

    statistics.getOrElse(1, 0) should be (2)
    statistics.getOrElse(2, 0) should be (1)
    statistics.getOrElse(3, 0) should be (2)
  }

  it should "validate a given grade" in {
    assessment.validateGrade(Grade(1, 2.75)) should be (true)
    assessment.validateGrade(Grade(1, 0.75)) should not be (true)
  }

  it should "validate its grades" in {
    var grades = new HashMap[Long, Grade]()
    grades.put(1, Grade(1, 2.75))
    grades.put(2, Grade(2, -0.75))

    Assessment(assessment.questions, assessment.answers, grades).validateGrades should be (true)

    grades = new HashMap[Long, Grade]()
    grades.put(1, Grade(1, 2.75))
    grades.put(2, Grade(2, 0.25))

    Assessment(assessment.questions, assessment.answers, grades).validateGrades should not be (true)
  }
}
