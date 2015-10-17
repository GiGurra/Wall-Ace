
lazy val root = (project in file(".")).
  settings(
    name := "wall-ace",
    organization := "se.gigurra",
    version := "SNAPSHOT",
    scalaVersion := "2.11.7",

    parallelExecution in Test := false,

    unmanagedSourceDirectories in Compile += baseDirectory.value / "src_generated" / "main" / "java",

    libraryDependencies ++= Seq(
        "com.novocode" % "junit-interface" % "0.11" % "test",
        "org.zeromq" % "jeromq" % "0.3.5",
		"org.slf4j" % "slf4j-simple" % "1.7.12",
        "se.culvertsoft" % "mgen-javalib" % "0.2.4",
        "org.scala-lang" % "scala-reflect" % scalaVersion.value,
        "com.nativelibs4java" %% "scalaxy-streams" % "0.3.4" % "provided",
		"org.jogamp.gluegen" % "gluegen-rt-main" % "2.2.4",
		"org.jogamp.jogl" % "jogl-all" % "2.2.4"
    )
  )


