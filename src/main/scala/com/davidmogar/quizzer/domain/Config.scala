package com.davidmogar.quizzer.domain

case class Config(questions: String = "",
                  answers: String = "",
                  output: String = "",
                  tests: String = "",
                  statistics: Boolean = false)
