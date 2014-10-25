package com.davidmogar.quizzer.deserializers

import com.davidmogar.quizzer.domain.{TrueFalseQuestion, MultichoiceQuestion}
import org.scalatest.{Matchers, BeforeAndAfter, FunSuite}


class AssessmentDeserializer$Test extends org.scalatest.FlatSpec with BeforeAndAfter with Matchers {

  val questionsJson = """
      { "questions":
        [ { "type": "multichoice",
        "id" : 1,
        "questionText": "Scala fue creado por...",
        "alternatives": [
        { "text": "Martin Odersky",   "code": 1, "value": 1 },
        { "text": "James Gosling",    "code": 2, "value": -0.25 },
        { "text": "Guido van Rossum", "code": 3, "value": -0.25 }
        ]
      },
        { "type" : "truefalse",
          "id" : 2,
          "questionText": "El creador de Ruby es Yukihiro Matsumoto",
          "correct": true,
          "valueOK": 1,
          "valueFailed": -0.25,
          "feedback": "Yukihiro Matsumoto es el principal desarrollador de Ruby desde 1996"
        }
        ]
      }
  """

  var answersJson = """
      { "items":
        [ { "studentId": 234 ,
        "answers":
        [ { "question" : 1, "value": 1 },
        { "question" : 2, "value": false }
        ]
      },
        { "studentId": 245 ,
          "answers":
          [ { "question" : 1, "value": 1 },
          { "question" : 2, "value": true }
          ]
        },
        { "studentId": 221 ,
          "answers":
          [ { "question" : 1, "value": 2 } ]
        }
        ]
      }
  """

  var gradesJson = """
      { "scores":
        [ { "studentId": 234, "value": 0.75 } ,
        { "studentId": 245, "value": 2.0 } ,
        { "studentId": 221, "value": 0.75 }
        ]
      }
  """

  "An AssessmentDeserializer" should "load answers correctly" in {
    val answers = AssessmentDeserializer.deserializeAnswers(answersJson)

    answers should not be null
    answers should have size 3
    answers.get(234).get should have size 2
    answers.get(245).get should have size 2
    answers.get(221).get should have size 1
  }

  it should "load grades correctly" in {
    val grades = AssessmentDeserializer.deserializeGrades(gradesJson)

    grades should not be null
    grades should have size 3
    grades.get(234).get.grade should be (0.75 +- 0.05)
    grades.get(245).get.grade should be (2.0 +- 0.05)
    grades.get(221).get.grade should be (0.75 +- 0.05)
  }

  it should "load questions correctly" in {
    val questions = AssessmentDeserializer.deserializeQuestions(questionsJson)

    questions should not be null
    questions should have size 2
    questions.get(1).get shouldBe a [MultichoiceQuestion]
    questions.get(2).get shouldBe a [TrueFalseQuestion]
    questions.get(2).get.asInstanceOf[TrueFalseQuestion].correct should be (true)

    val answers = AssessmentDeserializer.deserializeAnswers(answersJson)
    questions.get(1).get.getScore(answers.get(234).get(0)) should be (1.0 +- 0.05)
  }
}
