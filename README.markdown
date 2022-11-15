## Scalatra 

*This is a custom release for Tookitaki to resolve JSON4S conflicts with Spark and Scalatra versions*

Scalatra is a tiny, [Sinatra](http://www.sinatrarb.com/)-like web framework for
[Scala](http://www.scala-lang.org/).

## Example

```scala
import org.scalatra._

class ScalatraExample extends ScalatraServlet {
  get("/") {
    <h1>Hello, world!</h1>
  }
}
```

## Documentation

If you're just starting out, see the [installation](http://scalatra.org/getting-started/installation.html) and [first project](http://scalatra.org/getting-started/first-project.html) sections of our website.

Once you've done that, take a look at the [Scalatra Guides](http://scalatra.org/guides/) for documentation on all aspects of the framework, code examples, and more. We also have an extensive selection of [Example Applications](https://github.com/scalatra/scalatra-website-examples) which accompany the tutorials in the Scalatra Guides.

## Latest version

The latest version of Scalatra is `2.8.+`, and is published to [Maven Central](http://repo1.maven.org/maven2/org/scalatra).

```scala
libraryDependencies += "org.scalatra" %% "scalatra" % "2.8.+"
```

## Community

* Gitter: [Scalatra/Scalatra](https://gitter.im/scalatra/scalatra)
* Mailing list: [scalatra-user](http://groups.google.com/group/scalatra-user)
* IRC: #scalatra on irc.freenode.org
* [Guidelines for contributing](CONTRIBUTING.markdown)
