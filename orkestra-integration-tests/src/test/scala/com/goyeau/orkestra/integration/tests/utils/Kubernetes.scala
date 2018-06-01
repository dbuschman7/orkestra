package com.goyeau.orkestra.integration.tests.utils

import java.io.File
import java.util.UUID

import com.goyeau.kubernetesclient.{KubeConfig, KubernetesClient}
import io.k8s.api.core.v1.Namespace
import io.k8s.apimachinery.pkg.apis.meta.v1.ObjectMeta

import com.goyeau.orkestra.{kubernetes, OrkestraConfig}
import com.goyeau.orkestra.utils.AkkaImplicits._

object Kubernetes {
  val namespace = Namespace(
    metadata = Option(ObjectMeta(name = Option(s"orkestra-test-${UUID.randomUUID().toString.takeWhile(_ != '-')}")))
  )

  val configFile = new File(s"${System.getProperty("user.home")}/.kube/config")
  implicit val orkestraConfig = OrkestraConfig.fromEnvVars()
  val client =
    if (configFile.exists()) KubernetesClient(KubeConfig(configFile, "minikube"))
    else kubernetes.Kubernetes.client
}
