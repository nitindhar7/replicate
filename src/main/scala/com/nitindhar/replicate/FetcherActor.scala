package com.nitindhar.replicate

import akka.actor.{Actor, ActorLogging}
import com.nitindhar.replicate.Messages.{ReplicateData, FetchData}

class FetcherActor(
  host: String,
  port: String,
  db: String,
  collection: String,
  fields: List[String]
) extends Actor with ActorLogging {

  def receive = {
    case FetchData => {
      log.info(s"fetching $fields")
//      sender ! ReplicateData()
    }
  }

}
