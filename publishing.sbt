// Tookitaki build: published to Tookitaki's Artifactory (see TOOKITAKI-NOTICE.md)
ThisBuild / credentials += Credentials(Path.userHome / ".ivy2" / "credentials")
ThisBuild / publishMavenStyle := true
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := Some("Artifactory Realm".at("https://tookitaki-artifacts.tookitaki.com/artifactory/tookitaki-releases"))
Test / publishArtifact := false

// the modules FinCense uses (as published for 3.0.1-M2-TT)
addCommandAlias("publishTT", Seq("scalatra-common", "scalatra", "scalatra-auth", "scalatra-json", "scalatra-test", "scalatra-scalatest", "scalatra-swagger").map(_ + "/publish").mkString(";", ";", ""))
addCommandAlias("testTT", Seq("scalatra-common", "scalatra", "scalatra-auth", "scalatra-json", "scalatra-swagger").map(_ + "/test").mkString(";", ";", ""))
ThisBuild / resolvers += "Artifactory Realm" at "https://tookitaki-artifacts.tookitaki.com/artifactory/tookitaki-releases"
