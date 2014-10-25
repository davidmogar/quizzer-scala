package com.davidmogar.quizzer.deserializers

import org.scalatest.{Matchers, BeforeAndAfter}

class TestsDeserializer$Test extends org.scalatest.FlatSpec with BeforeAndAfter with Matchers {

  "A TestDeserializer" should "load all tests" in {
    try {
      val testsContent = io.Source.fromInputStream(getClass.getResourceAsStream("/tests.json")).mkString
      val tests = TestsDeserializer.deserialize(testsContent)

      tests should not be empty
      tests should have size 1
      tests(0).questionsUrl should be ("/questions.json")
      tests(0).gradesUrl should be ("/grades.json")
    } catch {
      case e: Exception => fail("Unexpected exception")
    }
  }

}
