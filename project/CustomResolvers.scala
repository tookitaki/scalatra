import sbt._

import scala.collection.immutable

object CustomResolvers {

  val Tookitaki =
    "Tookitaki"
      .at(
        "http://tookitaki-artifacts.tookitaki.com/artifactory/maven-local"
      )
      .withAllowInsecureProtocol(true)

  val TookitakiRelease =
    "TookitakiRelease"
      .at(
        "http://tookitaki-artifacts.tookitaki.com/artifactory/tookitaki-releases"
      )
      .withAllowInsecureProtocol(true)

  val RedHatGA                                              = "Redhat GA".at("https://maven.repository.redhat.com/ga/")
  val TypesafeReleases                                      = Classpaths.typesafeReleases
  val SonatypeRepoReleases: immutable.Seq[MavenRepository]  = Resolver.sonatypeOssRepos("releases")
  val SonatypeRepoSnapshots: immutable.Seq[MavenRepository] = Resolver.sonatypeOssRepos("snapshots")

  val CdhResolver =
    "Cloudera Maven Repository".at("https://repository.cloudera.com/artifactory/cloudera-repos")

  val sparkPackageResolver = "bintray-spark-packages".at("https://repos.spark-packages.org")

  val CdpResolver =
    "CDP".at("https://archive.cloudera.com/cdh7/7.1.8.0/maven-repository")

  val all :Seq[Resolver] = Seq(
    SonatypeRepoSnapshots,
    SonatypeRepoReleases,
    Seq(Tookitaki, TookitakiRelease, TypesafeReleases, RedHatGA, CdhResolver, CdpResolver, sparkPackageResolver)
  ).flatten
}
