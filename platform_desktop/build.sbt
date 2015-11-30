packSettings

lazy val mainClassName = "se.gigurra.wallace.platform.desktop.Main"

packMain := Map("platform_desktop" -> mainClassName)

mainClass in assembly := Some(mainClassName)
