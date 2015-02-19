package com.nitindhar.replicate

import akka.actor.{Actor, ActorLogging}
import com.nitindhar.replicate.Messages.{ReplicateData, FetchData}

class FetcherActor extends Actor with ActorLogging {

  def receive = {
    case FetchData(fields) => {
      log.info(s"fetching $fields")
      sender ! ReplicateData(List("a", "b"))
    }
  }

}
