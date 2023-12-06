//ThisBuild / publishTo := {
//  if (version.value.trim.endsWith("SNAPSHOT")) None
//  else Some(Opts.resolver.sonatypeStaging)
//}


// settings
credentials += Credentials(Path.userHome / ".ivy2" / "credentials")
publishConfiguration   := publishConfiguration.value.withOverwrite(true)
publishMavenStyle      := true
Test / publishArtifact := false
pomIncludeRepository   := { _ =>
  false
}
ThisBuild / publishTo  := Some(
  "Artifactory Realm"
    .at("http://tookitaki-artifacts.tookitaki.com/artifactory/tookitaki-releases")
    .withAllowInsecureProtocol(true)
)
