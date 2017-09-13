package com.drivetribe.orchestration.backend

import com.drivetribe.orchestration.Environment
import com.drivetribe.orchestration.infrastructure.DestroyEnvironment
import com.goyeau.orchestra.kubernetes.PodConfig
import com.goyeau.orchestra.parameter.Select
import com.goyeau.orchestra.{Job, _}
import com.typesafe.scalalogging.Logger
import shapeless.{::, HNil}

object SqlCopy {

  lazy val jobDefinition = Job[Environment :: Environment :: HNil]('sqlCopy)

  lazy val job = jobDefinition(PodConfig(MySqlContainer))(apply _)

  lazy val board =
    SingleJobBoard("SQL Copy", jobDefinition)(
      Select[Environment]("Source Environment", Environment, defaultValue = Option(Environment.Staging)),
      Select[Environment]("Destination Environment", Environment)
    )

  private lazy val logger = Logger(getClass)

  def apply(mySql: MySqlContainer.type)(source: Environment, destination: Environment): Unit = {
    mySql.dump(source, destination)
    DestroyEnvironment.job(destination).run
  }

//  def stage[T](name: String)(f: => T) =
//    f
}
