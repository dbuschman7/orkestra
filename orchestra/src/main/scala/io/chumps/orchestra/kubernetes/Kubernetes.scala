package io.chumps.orchestra.kubernetes

import java.io.File

import scala.io.Source

import io.chumps.orchestra.AkkaImplicits._
import io.chumps.orchestra.OrchestraConfig
import com.goyeau.kubernetesclient.KubernetesClient
import com.goyeau.kubernetesclient.KubeConfig

object Kubernetes {

  lazy val client = KubernetesClient(
    KubeConfig(
      server = OrchestraConfig.kubeUri,
      oauthToken = Option(Source.fromFile("/var/run/secrets/kubernetes.io/serviceaccount/token").mkString),
      caCertFile = Option(new File("/var/run/secrets/kubernetes.io/serviceaccount/ca.crt"))
    )
  )
}