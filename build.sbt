
lazy val renderer = RootProject(file("ext/gigurra-scala-2drenderer"))

lazy val root = (project in file(".")).
  settings(
    name := "wall-ace",
    organization := "se.gigurra",
    version := "SNAPSHOT",
    scalaVersion := "2.11.5",

    parallelExecution in Test := false,
    EclipseKeys.withSource := true,

    unmanagedSourceDirectories in Compile += baseDirectory.value / "src_generated" / "main" / "java",

    libraryDependencies ++= Seq(
        "com.novocode" % "junit-interface" % "0.11" % "test",
        "org.zeromq" % "jeromq" % "0.3.4",
        "se.culvertsoft" % "mgen-javalib" % "0.2.2",
        "org.scala-lang" % "scala-reflect" % scalaVersion.value,
        "com.nativelibs4java" %% "scalaxy-streams" % "0.3.4" % "provided",
        "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
        "org.scala-lang.modules" %% "scala-async" % "0.9.2"
    )
  )
  .dependsOn(renderer)


