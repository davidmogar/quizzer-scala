package com.davidmogar.quizzer

import java.net.URL

import com.davidmogar.quizzer.domain.{Grade, Config}
import com.davidmogar.quizzer.loaders.{AssessmentLoader, TestsLoader}
import com.davidmogar.quizzer.serializers.AssessmentSerializer

import scala.collection.mutable

/**
 * Helps to validate assessments, parsing questions and answers json files and generating new files with students'
 * grades and questions statistics.
 */
object Quizzer {

  /**
   * Calculate assessments' grades given the urls to the questions and answers files.
   *
   * @param questionsUrl URL to the questions file
   * @param answersUrl URL to the answers file
   * @return a new assessment with containing questions, answers and calculated grades
   */
  def calculateGrades(questionsUrl: URL, answersUrl: URL): Either[Assessment, String] = {
    try {
      val assessment = AssessmentLoader.loadAssessmentFromUrl(questionsUrl, answersUrl)
      if (assessment.isLeft) {
        assessment.left.get.calculateGrades
        Left(assessment.left.get)
      } else Right(assessment.right.get)
    } catch {
      case e: Exception => Right("A problem occurred while calculating grades: " + e.getMessage)
    }
  }

  /**
   * Show information about the assessment.
   *
   * @param assessment Assessment with the data
   * @param config Command line arguments
   */
  def showAssessment(assessment: Assessment, config: Config): Unit = {
    val format = if (config.output != "" && config.output == "xml") "xml" else "json"

    showGrades(assessment.grades, format)

    if (config.statistics) {
      showStatistics(assessment.getStatistics(), format)
    }
  }

  /**
   * Show the grades received as argument in the format specified.
   *
   * @param grades grades to show
   * @param format format of the output
   */
  def showGrades(grades: mutable.HashMap[Long, Grade], format: String): Unit = {
    println("Assessment's grades:")
    println(AssessmentSerializer.serializeGrades(grades, format) + "\n")
  }

  /**
   * Show the statistics received as argument in the format specified.
   *
   * @param statistics statistics to show
   * @param format format of the output
   */
  def showStatistics(statistics: mutable.HashMap[Long, Integer], format: String): Unit = {
    println("Assessment's statistics:")
    println(AssessmentSerializer.serializeStatistics(statistics, format) + "\n")
  }

  /**
   * Validate tests inside of the file referenced by the URL argument.
   *
   * @param url URL to the tests file
   */
  def validateAssessments(url: URL): Unit = {
    var valid = true;
    val tests = TestsLoader.loadTests(url)

    if (tests.isLeft) {
      for (test <- tests.left.get) {
        try {
          val assessment = AssessmentLoader.loadAssessmentFromUrl(new URL(test.questionsUrl),
            new URL(test.answersUrl),
            Option(new URL(test.gradesUrl)))
          if (assessment.isLeft) {
            if (assessment.left.get.validateGrades) {
              println("Test valid")
            } else {
              valid = false
              println("Test not valid")
            }
          }
        } catch {
          case e: Exception => println("Impossible to parse tests file"); valid = false
        }
      }
    }

    println(if (valid) "All tests OK" else "Tests failed")
  }

  def main (args: Array[String]) {
    val parser = new scopt.OptionParser[Config]("scopt") {
      opt[String]('q', "questions") action { (x, c) => c.copy(questions = x)} text ("URL to the questions file")
      opt[String]('a', "answers") action { (x, c) => c.copy(answers = x)} text ("URL to the answers file")
      opt[String]('o', "output") action { (x, c) => c.copy(output = x)} text ("Generate output in the specified " +
        "format (json or xml)")
      opt[String]('t', "tests") action { (x, c) => c.copy(tests = x)} text ("Validate assessments in tests file")
      opt[Unit]('s', "statistics") action { (_, c) => c.copy(statistics = true)} text ("Show questions statistics")
      help("help") text ("Show this help")
    }

    /* Parse command line arguments and decide what method to call next */
    try {
      parser.parse(args, Config()) map { config =>
        if (args.length == 0) {
          print("Server functionality is not supported in this project")
        } else if (config.tests != "") {
          validateAssessments(new URL(config.tests))
        } else if (config.answers != "" && config.questions != "") {
          val assessment = calculateGrades(new URL(config.questions), new URL(config.answers))

          if (assessment.isLeft) {
            showAssessment(assessment.left.get, config)
          }
        } else {
          parser.help("help")
        }
      } getOrElse {
        parser.help("help")
      }
    } catch {
      case e: Exception => println("There was a problem while parsing arguments")
    }
  }
}
