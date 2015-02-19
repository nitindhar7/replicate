package com.nitindhar.replicate

import akka.actor.{Actor, ActorLogging}
import com.nitindhar.replicate.Messages.{DataReplicationFailure, DataReplicationSuccess, FetchData, ReplicateData}

class ReplicateActor extends Actor with ActorLogging {

  def receive = {
    case ReplicateData(data) => {
      try {
        log.info(s"replicating $data")
        Thread.sleep(2000)
        sender ! DataReplicationSuccess
      } catch {
        case e: Exception => sender ! DataReplicationFailure("garbage")
      }
    }
  }

}
