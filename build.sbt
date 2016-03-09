val sharedSettings: Seq[Def.Setting[_]] = Seq(
  name := "wall-ace",
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


val ext_json4s = "org.json4s" %% "json4s-native" % "3.3.0"
val ext_rxScala = "io.reactivex" %% "rxscala" % "0.25.0"
val ext_kryonet = "com.esotericsoftware" % "kryonet" % "2.22.0-RC1"

val ext_libgdxVersion = "1.9.2"
val ext_libGdx = "com.badlogicgames.gdx" % "gdx" % ext_libgdxVersion
val ext_libGdx_freeType = "com.badlogicgames.gdx" % "gdx-freetype" % ext_libgdxVersion
val ext_libGdx_lwJgl = "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % ext_libgdxVersion
val ext_libGdx_platform_desktop = "com.badlogicgames.gdx" % "gdx-platform" % ext_libgdxVersion classifier "natives-desktop"
val ext_libGdx_freeType_platform_desktop = "com.badlogicgames.gdx" % "gdx-freetype-platform" % ext_libgdxVersion classifier "natives-desktop"

def subproject(id: String, deps: ClasspathDependency*)(extDeps: ModuleID*): Project = {
  Project(id, file(id)).settings(sharedSettings: _*).dependsOn(deps: _*).settings(
    name := s"${name.value}_${id}",
    libraryDependencies ++= extDeps
  )
}

val lib_util = subproject("lib_util")()
val lib_audio = subproject("lib_audio", lib_util)(ext_libGdx)
val lib_comm = subproject("lib_comm", lib_util)(ext_json4s, ext_rxScala, ext_kryonet)
val lib_input = subproject("lib_input", lib_util)(ext_libGdx)
val lib_render = subproject("lib_render", lib_util)(ext_libGdx, ext_libGdx_freeType)
val lib_stage = subproject("lib_stage", lib_util)()
val game_config = subproject("game_config", lib_util)()
val game_model = subproject("game_model", lib_util)()
val game_cursors = subproject("game_cursors", lib_util, lib_render)()
val game_client_stage_menu = subproject("game_client_stage_menu", lib_util, lib_stage, lib_input, lib_render, lib_audio, game_config, game_cursors)()
val game_client_stage_world = subproject("game_client_stage_world", lib_util, lib_stage, lib_input, lib_render, lib_audio, game_config, game_cursors, game_model)()
val game_client = subproject("game_client", lib_util, game_client_stage_menu, game_client_stage_world)()
val game_server = subproject("game_server", lib_util, lib_comm, game_model)()
val platform_desktop = subproject("platform_desktop", game_client)(ext_libGdx_lwJgl, ext_libGdx_platform_desktop, ext_libGdx_freeType_platform_desktop)

val all = Project("all", file("."))
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
