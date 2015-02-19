package com.nitindhar.replicate

import java.util.Date

object Messages {

  case class Start(config: ReplicateConfig)

  case class Read(collection: String, fields: Seq[String])

  case object StartReplication

  case class FetchData(fields: List[String])

  case class ReplicateData(data: List[String])

  case object DataReplicationSuccess

  case class DataReplicationFailure(datum: String)

}
