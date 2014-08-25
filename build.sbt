
scalaVersion := "2.11.2"

autoCompilerPlugins := true

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

libraryDependencies += compilerPlugin("org.scalamacros" %% "paradise" % "2.1.0-M1" cross CrossVersion.full)

