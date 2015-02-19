package com.nitindhar.replicate

import akka.actor.Actor
import com.nitindhar.replicate.Messages.Read

class PostgreSqlWriter extends Actor {

  def receive = {
    case Read(collection, fields) => ""
  }

}
