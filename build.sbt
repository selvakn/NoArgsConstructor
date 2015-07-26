
scalaVersion := "2.11.7"

autoCompilerPlugins := true

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

libraryDependencies += compilerPlugin("org.scalamacros" %% "paradise" % "2.1.0-M5" cross CrossVersion.full)

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.5" % "test"
