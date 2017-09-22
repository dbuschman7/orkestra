package com.drivetribe.orchestration.backend

import com.drivetribe.orchestration.Environment
import com.goyeau.orchestra._
import io.k8s.api.core.v1.Container

object MySqlContainer
    extends Container(name = "mysql", image = "mysql:5.7.18", tty = Option(true), command = Option(Seq("cat"))) {

  def dump(source: Environment, destination: Environment, dbName: String = "user_identity", params: String = "") = {
    mysql(destination, "", s"CREATE DATABASE IF NOT EXISTS $dbName")
    sh(
      s"""mysqldump \\
         |  -h ${source.entryName}-aurora-ro.drivetribe.com \\
         |  -u${System.getenv("AURORA_USERNAME")} -p${System.getenv("AURORA_PASSWORD")} \\
         |  --single-transaction \\
         |  --compress $params $dbName | \\
         |  mysql \\
         |    -h ${destination.entryName}-aurora.drivetribe.com \\
         |    -u${System.getenv("AURORA_USERNAME")} -p${System.getenv("AURORA_PASSWORD")} $dbName""".stripMargin,
      this
    )
  }

  def copySubset(source: Environment, destination: Environment, dbName: String = "user_identity") {
    mysql(destination, "", "CREATE DATABASE IF NOT EXISTS $dbName")
    dump(source, destination, dbName, "--tables devices magic_tokens mimics takeovers users")
    dump(source, destination, dbName, "--tables credentials --where 'registered = 1'")
  }

  def mysql(environment: Environment, dbName: String, command: String) =
    sh(
      s"""mysql \\
         |  -h ${environment.entryName}-aurora.drivetribe.com -u${System.getenv("AURORA_USERNAME")} \\
         |  -p${System.getenv("AURORA_PASSWORD")} \\
         |  -e '$command' $dbName""".stripMargin,
      this
    )

}
