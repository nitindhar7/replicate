package com.nitindhar.replicate

import akka.actor.{Props, ActorLogging, Actor}
import com.nitindhar.replicate.Messages._

import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

class ReplicationSupervisor extends Actor with ActorLogging {

  /*
      config.collections.foreach { c =>
        val router = replicator.actorOf(Props([FetcherActor], c.replicationConfig).withRouter(RoundRobinRouter(nrOfInstances = c.num)), name = c.name)
        replicator.scheduler.schedule(c.initdelay milliseconds, c.period milliseconds, router, StartReplication)
      }

      mapping: [
        {
          src: {
            kind: "mongodb",
            host: "localhost",
            port: "27017",
            db: "replicate",
            collection: "users"
            fields: ["name", "email"]
          },
          destination: {
            kind: "postgresql",
            host: "localhost",
            port: "1011",
            db: "datawarehouse",
            table: "users",
            fields: ["name", "email"]
          }
        }
      ]
     */

  def receive = {
    case StartReplication => {
      log.info("Starting Replication")
      val fetcher = context.actorOf(Props(classOf[FetcherActor], "localhost", "27018", "host-committee", "event_series", List("name", "email")))
      context.system.scheduler.schedule(3000 milliseconds, 5000 milliseconds, fetcher, FetchData)
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
