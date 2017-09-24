lazy val orchestra = crossProject
  .crossType(CrossType.Pure)
  .enablePlugins(ScalaJSPlugin, BuildInfoPlugin)
  .jvmSettings(crossVersion := Binary())
  .settings(
    name := "Orchestra",
    organization := "com.goyeau",
    scalaVersion := "2.12.3",
    scalacOptions += "-deprecation",
    buildInfoPackage := s"${organization.value}.orchestra",
    resolvers += Opts.resolver.sonatypeSnapshots,
    libraryDependencies ++= Seq(
      "com.chuusai" %%% "shapeless" % "2.3.2",
      "com.vmunier" %% "scalajs-scripts" % "1.1.1",
      "com.beachape" %%% "enumeratum" % "1.5.12" % Provided,
      "com.lihaoyi" %%% "autowire" % "0.2.6",
      "com.goyeau" %% "kubernetes-client" % "0.0.1+4-c3703e26+20170923-1722-SNAPSHOT"
    ) ++ scalaJsReact.value ++ akkaHttp.value ++ scalaCss.value ++ logging.value ++ circe.value,
    jsDependencies ++= Seq(
      "org.webjars.npm" % "ansi_up" % "2.0.2" / "ansi_up.js" commonJSName "ansi_up"
    ) ++ react.value,
    licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Option(url("https://github.com/joan38/orchestra")),
    scmInfo := Option(
      ScmInfo(
        url("https://github.com/joan38/orchestra"),
        "https://github.com/joan38/orchestra.git"
      )
    ),
    developers := List(
      Developer(id = "joan38", name = "Joan Goyeau", email = "joan@goyeau.com", url = url("http://goyeau.com"))
    ),
    publishTo := Option(
      if (isSnapshot.value) Opts.resolver.sonatypeSnapshots
      else Opts.resolver.sonatypeStaging
    ),
    publishMavenStyle := true
  )
lazy val orchestraJVM = orchestra.jvm
lazy val orchestraJS = orchestra.js

lazy val akkaHttp = Def.setting {
  val akkaHttpVersion = "10.0.10"
  Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test
  )
}

lazy val logging = Def.setting {
  Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )
}

lazy val scalaCss = Def.setting {
  val scalaCssVersion = "0.5.3"
  Seq(
    "com.github.japgolly.scalacss" %%% "core" % scalaCssVersion,
    "com.github.japgolly.scalacss" %%% "ext-react" % scalaCssVersion
  )
}

lazy val scalaJsReact = Def.setting {
  val scalaJsReactVersion = "1.1.0"
  Seq(
    "com.github.japgolly.scalajs-react" %%% "core" % scalaJsReactVersion,
    "com.github.japgolly.scalajs-react" %%% "extra" % scalaJsReactVersion
  )
}

lazy val react = Def.setting {
  val reactVersion = "15.6.1"
  Seq(
    "org.webjars.bower" % "react" % reactVersion / "react-with-addons.js" minified "react-with-addons.min.js" commonJSName "React",
    "org.webjars.bower" % "react" % reactVersion / "react-dom.js" minified "react-dom.min.js" dependsOn "react-with-addons.js" commonJSName "ReactDOM"
  )
}

lazy val circe = Def.setting {
  Seq(
    "circe-core",
    "circe-generic",
    "circe-parser",
    "circe-shapes",
    "circe-java8"
  ).flatMap { name =>
    val version = "0.9.0-M1"
    Seq(
      "io.circe" %%% name % version,
      "io.circe" %% name % version
    )
  }
}
