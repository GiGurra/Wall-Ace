val libgdxVersion = "1.7.0"

lazy val baseName = "wall-ace"

lazy val sharedSettings: Seq[Def.Setting[_]] = Seq(
  name := baseName,
  version := "0.1",
  scalaVersion := "2.11.7",
  resolvers += "clojars" at "http://clojars.org/repo",
  libraryDependencies ++= Seq(
    "com.nativelibs4java" %% "scalaxy-streams" % "0.3.4" % "provided",
    "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
    "com.jsuereth" %% "scala-arm" % "1.4"
  ),
  javacOptions ++= Seq(
    "-source", "1.7",
    "-target", "1.7"
  ),
  scalacOptions ++= Seq(
    "-Xlint",
    "-Ywarn-dead-code",
    //"-Ywarn-value-discard",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused",
    "-Ywarn-unused-import",
    "-unchecked",
    "-deprecation",
    "-feature",
    "-encoding", "UTF-8",
    "-target:jvm-1.7"
  ),
  cancelable := true,
  exportJars := true
)

lazy val util = project in file("util") settings (sharedSettings: _*) settings(
  name := baseName + "-util"
)

lazy val model = project in file("model") settings (sharedSettings: _*) dependsOn util settings(
  name := baseName + "-model"
)

lazy val comm = project in file("comm") settings (sharedSettings: _*) dependsOn util settings(
  name := baseName + "-comm",
  libraryDependencies ++= Seq(
    "org.json4s" %% "json4s-native" % "3.3.0",
    "io.reactivex" %% "rxscala" % "0.25.0",
    "com.esotericsoftware" % "kryonet" % "2.22.0-RC1"
  )
)

lazy val input = project in file("input") settings (sharedSettings: _*) dependsOn util settings(
  name := baseName + "-input",
  libraryDependencies ++= Seq(
    "com.badlogicgames.gdx" % "gdx" % libgdxVersion
  )
)

lazy val render = project in file("render") settings (sharedSettings: _*) dependsOn util settings(
  name := baseName + "-render",
  libraryDependencies ++= Seq(
    "com.badlogicgames.gdx" % "gdx" % libgdxVersion,
    "com.badlogicgames.gdx" % "gdx-freetype" % libgdxVersion
  )
)

lazy val client = project in file("client") settings (sharedSettings: _*) dependsOn(util, model, comm, render, input) settings(
  name := baseName + "-client"
)

lazy val server = project in file("server") settings (sharedSettings: _*) dependsOn(util, model, comm) settings(
  name := baseName + "-server"
)

lazy val desktop = project in file("desktop") settings (sharedSettings: _*) dependsOn client settings(
    name := baseName + "-desktop",
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % libgdxVersion,
      "com.badlogicgames.gdx" % "gdx-platform" % libgdxVersion classifier "natives-desktop",
      "com.badlogicgames.gdx" % "gdx-freetype-platform" % libgdxVersion classifier "natives-desktop"
    ),
    fork in Compile := true
  )

lazy val all = project in file(".") aggregate(util, model, comm, render, input, client, server, desktop)