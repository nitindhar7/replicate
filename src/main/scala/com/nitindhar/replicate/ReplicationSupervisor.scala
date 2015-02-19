package com.nitindhar.replicate

import akka.actor.{Props, ActorLogging, Actor}
import com.nitindhar.replicate.Messages._

import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

class ReplicationSupervisor extends Actor with ActorLogging {

  def receive = {
    case StartReplication => {
      log.info("Starting Replication")
      val fetcher = context.actorOf(Props[FetcherActor])
      context.system.scheduler.schedule(3000 milliseconds, 5000 milliseconds, fetcher, FetchData(List("name", "email")))
    }
    case ReplicateData(data) => {
      log.info(s"Will repicate $data")
      val replicator = context.actorOf(Props[ReplicateActor])
      replicator ! ReplicateData(data)
    }
    case DataReplicationSuccess => log.info("Data replicated successfully!")
    case DataReplicationFailure(datum) => log.error(s"Data [$datum] replication failed")
  }

}
