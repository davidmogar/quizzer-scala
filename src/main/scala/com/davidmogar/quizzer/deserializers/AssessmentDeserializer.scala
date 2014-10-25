package com.davidmogar.quizzer.deserializers

import com.davidmogar.quizzer.domain._
import play.api.libs.json.{JsObject, Json}

import scala.collection.mutable

object AssessmentDeserializer {

  def deserializeAnswers(json: String): mutable.HashMap[Long, List[Answer]] = {
    val answers = new mutable.HashMap[Long, List[Answer]]()
    val data = Json.parse(json)

    for (item <- (data \ "items").as[List[JsObject]]) {
      val studentId = (item \ "studentId").as[Long]
      var studentAnswers = List[Answer]()

      for (answer <- (item \ "answers").as[List[JsObject]]) {
        studentAnswers ::= Answer((answer \ "question").as[Long], (answer \ "value").toString)
      }

      answers.put(studentId, studentAnswers)
    }

    return answers
  }

  def deserializeGrades(json: String): mutable.HashMap[Long, Grade] = {
    val grades = new mutable.HashMap[Long, Grade]()
    val data = Json.parse(json)

    for (score <- (data \ "scores").as[List[JsObject]]) {
      val studentId = (score \ "studentId").as[Long]

      grades.put(studentId, Grade(studentId, (score \ "value").as[Double]))
    }

    return grades
  }

  def deserializeQuestions(json: String): mutable.HashMap[Long, Question] = {
    var questions = new mutable.HashMap[Long, Question]()
    var data = Json.parse(json)

    for (question <- (data \ "questions").as[List[JsObject]]) {
      val v = (question \ "type").as[String]
      val newQuestion = v match {
        case "multichoice" => deserializeMultichoice(question)
        case "numerical" => deserializeNumerical(question)
        case "truefalse" => deserializeTrueFalse(question)
      }

      questions.put((question \ "id").as[Long], newQuestion)
    }

    return questions
  }

  def deserializeMultichoice(data: JsObject): MultichoiceQuestion = {
    val id = (data \ "id").as[Long]
    val text = (data \ "questionText").as[String]
    val question = MultichoiceQuestion(id, text)

    for (alternative <- (data \ "alternatives").as[List[JsObject]]) {
      question.addAlternative((alternative \ "code").as[Long],
          (alternative \ "text").as[String], (alternative \ "value").as[Double])
    }

    return question
  }

  def deserializeNumerical(data: JsObject): NumericalQuestion = {
    val id = (data \ "id").as[Long]
    val text = (data \ "questionText").as[String]
    val correct = (data \ "correct").as[Double]
    val valueCorrect = (data \ "valueOK").as[Double]
    val valueIncorrect = (data \ "valueFailed").as[Double]

    return NumericalQuestion(id, text, correct, valueCorrect, valueIncorrect)
  }

  def deserializeTrueFalse(data: JsObject): TrueFalseQuestion = {
    val id = (data \ "id").as[Long]
    val text = (data \ "questionText").as[String]
    val correct = (data \ "correct").as[Boolean]
    val valueCorrect = (data \ "valueOK").as[Double]
    val valueIncorrect = (data \ "valueFailed").as[Double]
    val feedback = (data \ "feedback").as[String]

    return TrueFalseQuestion(id, text, correct, valueCorrect, valueIncorrect, feedback)
  }
}
