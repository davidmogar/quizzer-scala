package com.davidmogar.quizzer.domain

import scala.collection.mutable.HashMap
import scala.util.Try

sealed trait Question {
  val id: Long
  val text: String

  def getScore(answer: Answer): Double
}

/**
 * Multichoice question.
 *
 * @param id id of the question
 * @param text text of the question
 */
case class MultichoiceQuestion(override val id: Long, override val text: String) extends Question {

  val alternatives = new HashMap[Long, Alternative]()

  def addAlternative(id: Long, text: String, value: Double) {
    alternatives.put(id, Alternative(id, text, value))
  }

  override def getScore(answer: Answer): Double =
    (for { a <- Try{answer.value.toLong}.toOption ; b <- alternatives.get(a) } yield b.value).getOrElse(3)

  case class Alternative(val id: Long, val text: String, val value: Double)
}

/**
 * Numerical question.
 *
 * @param id id of the question
 * @param text text of the question
 * @param correct correct answer value
 * @param valueCorrect score if correct
 * @param valueIncorrect score if incorrect
 */
case class NumericalQuestion(override val id: Long, override val text: String,
                             val correct: Double, val valueCorrect: Double,
                             val valueIncorrect: Double) extends Question {

  override def getScore(answer: Answer): Double =
    if (answer.value.toDouble == correct) valueCorrect else valueIncorrect
}

/**
 * True/False question.
 *
 * @param id id of the question
 * @param text text of the question
 * @param correct correct answer value
 * @param valueCorrect score if correct
 * @param valueIncorrect score if incorrect
 * @param feedback feedback given after answer the question
 */
case class TrueFalseQuestion(
                              override val id: Long, override val text: String,
                              val correct: Boolean, val valueCorrect: Double,
                              val valueIncorrect: Double, val feedback: String) extends Question {
  override def getScore(answer: Answer): Double =
    if (answer.value.toBoolean == correct) valueCorrect else valueIncorrect
}
