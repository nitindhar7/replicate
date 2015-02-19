package com.nitindhar.replicate

import akka.actor.{Actor, ActorLogging}
import com.nitindhar.replicate.Messages.{Start, Read}

/**
 * Need to initialize this with config for mongodb
 */
class MongoReader extends Actor {

  def receive = {
    case Read(collection, fields) => ""
  }

}
