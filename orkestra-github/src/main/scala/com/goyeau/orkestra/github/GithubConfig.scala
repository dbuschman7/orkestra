package com.goyeau.orkestra.github

import akka.http.scaladsl.model.Uri

import com.goyeau.orkestra.OrkestraConfig

case class GithubConfig(uri: Uri, port: Int, token: String)

object GithubConfig {
  def fromEnvVars() = GithubConfig(
    fromEnvVar("URI").fold(throw new IllegalStateException("ORKESTRA_GITHUB_URI should be set"))(Uri(_)),
    fromEnvVar("PORT").map(_.toInt).getOrElse(8081),
    fromEnvVar("TOKEN").getOrElse(throw new IllegalStateException("ORKESTRA_GITHUB_TOKEN should be set"))
  )

  def fromEnvVar(envVar: String) = OrkestraConfig.fromEnvVar(s"GITHUB_$envVar")
}
