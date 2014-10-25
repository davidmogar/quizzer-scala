package com.davidmogar.quizzer.loaders

import org.scalatest.{Matchers, BeforeAndAfter, FunSuite}

class TestsLoader$Test extends org.scalatest.FlatSpec with BeforeAndAfter with Matchers {

  "A TestsLoader" should "load all test" in {
    val testsUrl = getClass.getResource("/tests.json")

    testsUrl should not be null

    try {
      val tests = TestsLoader.loadTests(testsUrl)
      tests should not be null
      tests.isLeft should be (true)
      tests.left.get should have size 1
    } catch {
      case e: Exception => fail("Exception not expected")
    }
  }
}
