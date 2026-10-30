# Tookitaki build of Scalatra (3.0.1-M2-TT3)

Upstream Scalatra 3.0.0-M2 (https://github.com/scalatra/scalatra, BSD-2-Clause, see `LICENSE`; jakarta servlet 5),
modified by Tookitaki:

- json4s dependency switched from `org.json4s` to Tookitaki's shaded build `com.tookitaki.shaded.org.json4s` 4.0.7-TT
  (imports in `scalatra-json` and `scalatra-swagger` changed accordingly);
- version `3.0.1-M2-TT3`, published to Tookitaki's Artifactory (`tookitaki-releases`), Scala 2.12 and 2.13.

Replaces `3.0.1-M2-TT` and `3.0.1-M2-TT2` (TT2 used json4s 4.0.7, whose jar names collide with upstream) (same code on `com.tookitaki.json4s` 4.0.5-TT). Build: `sbt "++2.12.16" publishTT "++2.13.8" publishTT`.
