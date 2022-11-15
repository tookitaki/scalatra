import com.jsuereth.sbtpgp.PgpKeys
import scala.xml._
import java.net.URL
import Dependencies._

val unusedOptions = Seq("-Ywarn-unused:imports")

lazy val tookitaki = "Tookitaki"
  .at("http://tookitaki-artifacts.tookitaki.com/artifactory/tookitaki-releases")
  .withAllowInsecureProtocol(true)
lazy val allResolvers: Seq[Resolver] = Seq(tookitaki)

lazy val scalatraSettings = Seq(
  organization := "org.scalatra",
  resolvers ++= allResolvers,
  Test / fork := true,
  Test / baseDirectory := (ThisBuild / baseDirectory).value,
  crossScalaVersions := Seq("2.12.16", "2.13.8", "3.1.3"),
  scalaVersion := crossScalaVersions.value.head,
  libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % "always",
  Test / testOptions ++= {
    if (scalaBinaryVersion.value == "3") {
      Seq(
        Tests.Exclude(Set(
          "org.scalatra.swagger.ModelSpec",
          "org.scalatra.swagger.SwaggerSpec2",
          "org.scalatra.swagger.ModelCollectionSpec",
        )),
      )
    } else {
      Nil
    }
  },
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, _)) =>
        unusedOptions ++ Seq(
          "-target:jvm-1.8",
          "-Xlint",
          "-Xcheckinit",
        )
      case _ =>
        Seq(
          "-source",
          "3.0-migration",
        )
    }
  },
  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    /*"-Yinline-warnings",*/
    "-encoding", "utf8",
    "-feature",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:existentials"
  ),
  manifestSetting,
) ++ publishOpts ++ Seq(Compile, Test).flatMap(c =>
  c / console / scalacOptions --= unusedOptions
)

lazy val scalatraProject = Project(
  id = "scalatra-project",
  base = file(".")).settings(
    scalatraSettings ++ Seq(
    name := "scalatra-unidoc",
    artifacts := Classpaths.artifactDefs(Seq(Compile / packageDoc, Compile / makePom)).value,
    packagedArtifacts := Classpaths.packaged(Seq(Compile / packageDoc, Compile / makePom)).value,
    description := "A tiny, Sinatra-like web framework for Scala",
    shellPrompt := { state =>
      s"sbt:${Project.extract(state).currentProject.id}" + Def.withColor("> ", Option(scala.Console.CYAN))
    }
  ) ++ Defaults.packageTaskSettings(
    Compile / packageDoc, (Compile / unidoc).map(_.flatMap(Path.allSubpaths))
  )).aggregate(
    scalatraCore,
    scalatraAuth,
    scalatraForms,
    scalatraTwirl,
    scalatraJson,
    scalatraTest,
    scalatraScalatest,
    scalatraSpecs2,
    scalatraSwagger,
    scalatraJetty,
    scalatraCommon,
    scalatraMetrics,
    scalatraCache,
  ).enablePlugins(ScalaUnidocPlugin)

lazy val scalatraCommon = Project(
  id = "scalatra-common",
  base = file("common")).settings(
    scalatraSettings ++ Seq(
    libraryDependencies ++= Seq(servletApi % "provided,test")
  )
)

lazy val scalatraCore = Project(
  id = "scalatra",
  base = file("core")).settings(scalatraSettings ++ Seq(
    libraryDependencies ++= Seq(
      servletApi % "provided;test",
      slf4jApi,
      jUniversalChardet,
      commonsText,
      parserCombinators,
      xml,
      collectionCompact,
      akkaActor % "test",
      akkaTestkit % "test"
    ),
    description := "The core Scalatra framework"
  )
) dependsOn(
  scalatraSpecs2 % "test->compile",
  scalatraScalatest % "test->compile",
  scalatraCommon % "compile;test->test"
)

lazy val scalatraAuth = Project(
  id = "scalatra-auth",
  base = file("auth")).settings(
    scalatraSettings ++ Seq(
    description := "Scalatra authentication module"
  )
) dependsOn(scalatraCore % "compile;test->test;provided->provided")

lazy val scalatraTwirl = Project(
  id = "scalatra-twirl",
  base = file("twirl")).settings(
  scalatraSettings ++ Seq(
    libraryDependencies += twirlApi,
    description := "Twirl integration with Scalatra"
  )
) dependsOn(scalatraCore  % "compile;test->test;provided->provided")

lazy val scalatraJson = Project(
  id = "scalatra-json",
  base = file("json")).settings(
    scalatraSettings ++ Seq(
    description := "JSON support for Scalatra",
    libraryDependencies ++= Seq(
      json4sJackson % "provided",
      json4sNative % "provided",
      json4sCore,
      json4sXml
    )
  )
) dependsOn(scalatraCore % "compile;test->test;provided->provided")

lazy val scalatraForms = Project(
  id = "scalatra-forms",
  base = file("forms")).settings(
    scalatraSettings ++ Seq(
    description := "Data binding and validation for Scalatra"
  )
) dependsOn(scalatraCore % "compile;test->test;provided->provided")

lazy val scalatraJetty = Project(
  id = "scalatra-jetty",
  base = file("jetty")).settings(
    scalatraSettings ++ Seq(
    libraryDependencies ++= Seq(
      servletApi,
      jettyServlet
    ),
    description := "Embedded Jetty server for Scalatra apps"
  )
) dependsOn(scalatraCore % "compile;test->test;provided->provided")

lazy val scalatraTest = Project(
  id = "scalatra-test",
  base = file("test")).settings(
    scalatraSettings ++ Seq(
    libraryDependencies ++= Seq(
      jettyWebapp,
      servletApi,
      mockitoAll,
      httpclient,
      httpmime
    ) ++ specs2.map(_ % "test"),
    description := "The abstract Scalatra test framework"
  )
) dependsOn(scalatraCommon % "compile;test->test;provided->provided")

lazy val scalatraScalatest = Project(
  id = "scalatra-scalatest",
  base = file("scalatest")).settings(
    scalatraSettings ++ Seq(
    libraryDependencies ++= scalatest,
    libraryDependencies ++= Seq(
      scalatestJunit,
      junit
    ),
    description := "ScalaTest support for the Scalatra test framework"
  )
) dependsOn(scalatraTest % "compile;test->test;provided->provided")

lazy val scalatraSpecs2 = Project(
  id = "scalatra-specs2",
  base = file("specs2")).settings(
    scalatraSettings ++ Seq(
    libraryDependencies ++= specs2,
    description := "Specs2 support for the Scalatra test framework"
  )
) dependsOn(scalatraTest % "compile;test->test;provided->provided")

lazy val scalatraSwagger = Project(
  id = "scalatra-swagger",
  base = file("swagger")).settings(
    scalatraSettings ++ Seq(
    libraryDependencies ++= Seq(
      parserCombinators,
      logbackClassic % "provided"
    ),
    description := "Scalatra integration with Swagger"
  )
) dependsOn(
  scalatraCore % "compile;test->test;provided->provided",
  scalatraJson % "compile;test->test;provided->provided",
  scalatraAuth % "compile;test->test"
)

lazy val scalatraMetrics = Project(
  id = "scalatra-metrics",
  base = file("metrics")).settings(
    scalatraSettings ++ Seq(
    libraryDependencies ++= Seq(
      metricsScala,
      metricsServlets,
      metricsServlet
    ),
    description := "Scalatra integration with Metrics"
  )
) dependsOn(scalatraCore % "compile;test->test;provided->provided")

lazy val scalatraCache = Project(
  id = "scalatra-cache",
  base = file("cache")).settings(
    scalatraSettings ++ Seq(
    libraryDependencies ++= Seq(
      googleGuava
    ),
    description := "Scalatra Cache support"
  )
) dependsOn(scalatraCore % "compile;test->test;provided->provided")

//lazy val scalatraCacheGuava = Project(
//  id = "scalatra-cache-guava",
//  base = file("cache-guava")).settings(
//    scalatraSettings ++ Seq(
//    libraryDependencies ++= Seq(
//      googleGuava,
//      googleFindBugs
//    ),
//    description := "Scalatra Cache integration with Google Guava"
//  )
//) dependsOn(
//  scalatraCore % "compile;test->test;provided->provided",
//  scalatraCache % "compile;test->test;provided->provided"
//)

lazy val manifestSetting = packageOptions += {
  Package.ManifestAttributes(
    "Created-By" -> "Simple Build Tool",
    "Built-By" -> System.getProperty("user.name"),
    "Build-Jdk" -> System.getProperty("java.version"),
    "Specification-Title" -> name.value,
    "Specification-Version" -> version.value,
    "Specification-Vendor" -> organization.value,
    "Implementation-Title" -> name.value,
    "Implementation-Version" -> version.value,
    "Implementation-Vendor-Id" -> organization.value,
    "Implementation-Vendor" -> organization.value
  )
}


lazy val publishOpts = Seq(
  // settings
  credentials += Credentials(Path.userHome / ".ivy2" / "credentials"),
  publishConfiguration := publishConfiguration.value.withOverwrite(true),
  publishMavenStyle := true,
  Test / publishArtifact := false,
  pomIncludeRepository := { _ => false },
  ThisBuild / publishTo := Some(
    "Artifactory Realm"
      .at("http://tookitaki-artifacts.tookitaki.com/artifactory/tookitaki-releases")
      .withAllowInsecureProtocol(true)
  )
)

lazy val doNotPublish = Seq(publish := {}, publishLocal := {}, PgpKeys.publishSigned := {}, PgpKeys.publishLocalSigned := {})
