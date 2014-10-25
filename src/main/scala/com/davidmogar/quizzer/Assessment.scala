package com.davidmogar.quizzer

import com.davidmogar.quizzer.domain.{Grade, Answer, Question}

import scala.collection.mutable.HashMap
import scala.util.control.Breaks._

case class Assessment(questions: HashMap[Long, Question],
                      answers: HashMap[Long, List[Answer]],
                      grades: HashMap[Long, Grade]) {

  /**
   * Calculate the grades of this assessment.
   */
  def calculateGrades(): Unit = {
    grades.clear()
    answers.keySet.foreach {studentId => grades.put(studentId, Grade(studentId, calculateStudentGrade(studentId)))}
  }

  /**
   * Calculates the grade of a given student.
   *
   * @param studentId id of the student
   * @return calculated grade of the student
   */
  def calculateStudentGrade(studentId: Long): Double = {
    var grade: Double = 0

    if (answers.contains(studentId)) {
      for (answer <- answers.get(studentId).get) {
        val questionId: Long = answer.questionId
        if (questions.contains(questionId)) {
          grade += questions.get(questionId).get.getScore(answer)
        }
      }
    }

    return grade
  }

  /**
   * Returns a HashMap mapping each question id with the number of correct answers of that question.
   *
   * @return a HashMap with the questions' statistics
   */
  def getStatistics(): HashMap[Long, Integer] = {
    val statistics = new HashMap[Long, Integer]()

    for (studentId <- answers.keySet) {
      for (answer <- answers.get(studentId).get) {
        val questionId: Long = answer.questionId
        if (questions.contains(questionId)) {
          if (questions.get(questionId).get.getScore(answer) > 0) {
            if (statistics.contains(questionId))
              statistics.put(questionId, statistics.get(questionId).get + 1)
            else
              statistics.put(questionId, 1)
          }
        }
      }
    }

    return statistics
  }

  /**
   * Validates the grade received as argument, checking that the value stored correspond to the grade obtained by
   * the student.
   *
   * @param grade grade to validate
   * @return <code>true</code> if the grade is valid, <code>false</code> otherwise
   */
  def validateGrade(grade: Grade): Boolean = {
    if (grade != null) grade.grade == calculateStudentGrade(grade.studentId) else false
  }

  /**
   * Validate all the grades of this assessment, checking that all the values stored in each grade correspond to
   * the actual grade obtained by the students.
   *
   * @return <code>true</code> if all the grades are valid, <code>false</code> otherwise
   */
  def validateGrades: Boolean = {
    var valid: Boolean = true

    breakable {
      for (grade <- grades.values) {
        valid = validateGrade(grade)
        if (!valid) break
      }
    }

    return valid
  }
}

object Assessment {
  def apply(): Assessment = new Assessment(new HashMap[Long, Question](),
    new HashMap[Long, List[Answer]](), new HashMap[Long,Grade]())

  def apply(questions: HashMap[Long, Question], answers: HashMap[Long, List[Answer]]): Assessment =
    new Assessment(questions, answers, new HashMap[Long,Grade]())
}
