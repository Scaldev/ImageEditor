// Version de Scala
scalaVersion := "3.3.1"

scalacOptions ++= Seq("-deprecation", "-no-indent")

libraryDependencies += "org.scalameta" %% "munit-scalacheck" % "0.7.29" % Test
