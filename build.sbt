import java.io.File

val libgdxVersion = "1.7.0"

lazy val sharedSettings: Seq[Def.Setting[_]] = Seq(
  name := "wall-ace",
  version := "0.1",
  scalaVersion := "2.11.7",
  assetsDirectory := {
    val r = file("core/src/main/resources")
    IO.createDirectory(r)
    r
  },
  resolvers += "clojars" at "http://clojars.org/repo",
  libraryDependencies ++= Seq(
    "com.nativelibs4java" %% "scalaxy-streams" % "0.3.4" % "provided",
    "com.esotericsoftware" % "kryonet" % "2.22.0-RC1",
    "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
    "org.json4s" %% "json4s-native" % "3.3.0",
    "io.reactivex" %% "rxscala" % "0.25.0",
    "com.jsuereth" %% "scala-arm" % "1.4"
  ),
  javacOptions ++= Seq(
    "-Xlint",
    "-encoding", "UTF-8",
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

lazy val core = project in file("core") settings (sharedSettings: _*)

lazy val client = project in file("client") settings (sharedSettings: _*) dependsOn core settings(
  name := (name in core).value + "-client",
  libraryDependencies ++= Seq(
    "com.badlogicgames.gdx" % "gdx" % libgdxVersion,
    "com.badlogicgames.gdx" % "gdx-freetype" % libgdxVersion
  )
)

lazy val server = project in file("server") settings (sharedSettings: _*) dependsOn core settings(
  name := (name in core).value + "-server"
)

lazy val desktop = project in file("desktop") settings (sharedSettings: _*) dependsOn client settings(
    name := (name in core).value + "-desktop",
    libraryDependencies ++= Seq(
     /* "net.sf.proguard" % "proguard-base" % "5.1" % "provided",*/
      "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % libgdxVersion,
      "com.badlogicgames.gdx" % "gdx-platform" % libgdxVersion classifier "natives-desktop",
      "com.badlogicgames.gdx" % "gdx-freetype-platform" % libgdxVersion classifier "natives-desktop"
    ),
    fork in Compile := true,
    baseDirectory in run := assetsDirectory.value
  )

lazy val assetsDirectory = settingKey[File]("Directory with game's assets")

lazy val nativesDirectory = settingKey[File]("Directory where android natives are extracted to")

lazy val extractNatives = taskKey[Unit]("Extracts natives to nativesDirectory")

lazy val assembly = TaskKey[Unit]("assembly", "Assembly desktop using Proguard")

lazy val all = project in file(".") aggregate(core, client, server, desktop)