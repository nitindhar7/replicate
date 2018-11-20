package com.nitindhar.replicate.experimental

import akka.actor.{ActorLogging, Actor}

trait ReplicatorConfig {
  val source: String
  val destination: String
}

trait Replicator {
  self: ReplicatorConfig =>

  def start: Unit

  def stop: Unit

}

trait ReplicatorActor extends Actor with ActorLogging with Replicator with ReplicatorConfig {

  import ReplicatorMessages._

  override def start = self ! StartReplication

  override def stop = context.stop(self)

  def receive = {
    case StartReplication => log.info("starting replication")
  }

}

object ReplicatorMessages {

  case object StartReplication

}
