package com.nitindhar.replicate.config

trait Storage {
  val kind: String
  val storageConfig: StorageConfig
}

trait StorageConfig {
  val host: String
  val port: String
  val db: String
}

object StorageConfig {

  def apply(kind: String, host: String, port: String, db: String) = kind match {
    case "MongoDbConfig" => MongoDbConfig(host, port, db)
    case "PostgreSqlConfig" => PostgreSqlConfig(host, port, db)
  }

}

case class MongoDbConfig(host: String, port: String, db: String) extends StorageConfig

case class PostgreSqlConfig(host: String, port: String, db: String) extends StorageConfig
