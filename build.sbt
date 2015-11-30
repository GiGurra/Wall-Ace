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

def subproject(projectName: String): Project = {
  Project(projectName, file(projectName)).settings(sharedSettings: _*).settings(
    name := s"${baseName}_${projectName}"
  )
}

lazy val lib_util = subproject("lib_util")

lazy val game_config = subproject("game_config") dependsOn lib_util

lazy val game_model = subproject("game_model") dependsOn lib_util

lazy val lib_audio = subproject("lib_audio") dependsOn lib_util settings(
  libraryDependencies ++= Seq(
    "com.badlogicgames.gdx" % "gdx" % libgdxVersion
  )
)

lazy val lib_comm = subproject("lib_comm") dependsOn lib_util settings(
  libraryDependencies ++= Seq(
    "org.json4s" %% "json4s-native" % "3.3.0",
    "io.reactivex" %% "rxscala" % "0.25.0",
    "com.esotericsoftware" % "kryonet" % "2.22.0-RC1"
  )
)

lazy val lib_input = subproject("lib_input") dependsOn lib_util settings(
  libraryDependencies ++= Seq(
    "com.badlogicgames.gdx" % "gdx" % libgdxVersion
  )
)

lazy val lib_render = subproject("lib_render") dependsOn lib_util settings(
  libraryDependencies ++= Seq(
    "com.badlogicgames.gdx" % "gdx" % libgdxVersion,
    "com.badlogicgames.gdx" % "gdx-freetype" % libgdxVersion
  )
)

lazy val lib_stage = subproject("lib_stage") dependsOn(lib_util)

lazy val game_cursors = subproject("game_cursors") dependsOn(lib_util, lib_render)

lazy val game_client_stage_menu = subproject("game_client_stage_menu") dependsOn(lib_util, lib_stage, lib_input, lib_render, lib_audio, game_config, game_cursors)

lazy val game_client_stage_world = subproject("game_client_stage_world") dependsOn(lib_util, lib_stage, lib_input, lib_render, lib_audio, game_config, game_cursors, game_model)

lazy val game_client = subproject("game_client") dependsOn(lib_util, game_client_stage_menu, game_client_stage_world)

lazy val game_server = subproject("game_server") dependsOn(lib_util, lib_comm, game_model)

lazy val platform_desktop = subproject("platform_desktop") dependsOn game_client settings(
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % libgdxVersion,
      "com.badlogicgames.gdx" % "gdx-platform" % libgdxVersion classifier "natives-desktop",
      "com.badlogicgames.gdx" % "gdx-freetype-platform" % libgdxVersion classifier "natives-desktop"
    ),
    fork in Compile := true
  )

lazy val all = Project("all", file("."))
  .aggregate(
    lib_util,
    lib_audio,
    lib_comm,
    lib_input,
    lib_render,
    lib_stage,
    game_config,
    game_model,
    game_cursors,
    game_client_stage_menu,
    game_client_stage_world,
    game_client,
    game_server,
    platform_desktop
  )

