package com.nitindhar.replicate

import java.util.Date

object Messages {

  case class Read(collection: String, fields: Seq[String])

  case object StartReplication

  case object FetchData

  case class ReplicateData(data: List[String])

  case object DataReplicationSuccess

  case class DataReplicationFailure(datum: String)

}
