name := "Quizzer"

version := "1.0"

resolvers += Resolver.sonatypeRepo("public")

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.github.scopt" %% "scopt" % "3.2.0"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.3.4"

libraryDependencies += "org.scalatra" %% "scalatra" % "2.3.0"

libraryDependencies += "javax.servlet" % "servlet-api" % "2.5"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test"