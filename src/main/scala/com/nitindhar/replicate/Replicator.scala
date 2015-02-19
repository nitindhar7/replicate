package com.nitindhar.replicate

import java.util.Date

import akka.actor.{ActorLogging, Actor, Props, ActorSystem}
import akka.routing.RoundRobinRouter
import com.nitindhar.replicate.Messages.{StartReplication, FetchData, Start, Read}
import com.nitindhar.replicate.{ReplicateConfig, MongoReader}

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration._

/**
 * Architecture
 *
 *  db -> reader - -.           Queue        .- - > writers -> db
 *  db -> reader - -+- - -> [ * * * * * ] - -+- - > writers -> db
 *  db -> reader - -'                        '- - > writers -> db
 *
 * Config
 *
 * {
 *   readers:
 *     num_of_readers: 1
 *     initial_period: 10
 *     frequency: 20
 *   queue:
 *
 *   writers:
 *     num_of_readers: 1
 *     initial_period: 10
 *     frequency: 20
 * }
 *
 *  - Readers
 *      # of readers
 *      initial period
 *      frequency
 *  - Queue
 *      type of queue
 *  - Writers
 *      # of readers
 *      initial period
 *      frequency
 *
 * API
 *
 * onStart:
 *    ramp up of readers & writers
 *
 *    Replicate.start(config)
 *
 * onShutdown:
 *    drainage of readers & writers
 *
 *    Replicate.stop
 *
 *
 *
 *
 *
 * val readers = replicator.actorOf(Props[MongoReader].withRouter(
      RoundRobinRouter(nrOfInstances = 10)
    ), name = "simpleRoutedActor")

 //  private val replicator = ActorSystem("replicator")


 http://doc.akka.io/docs/akka/2.0/scala/scheduler.html
 */
object Replicator {

  private lazy val ReplicateSystem = ActorSystem("ReplicateSystem")

  def start = {
    val supervisor = ReplicateSystem.actorOf(Props[ReplicationSupervisor])
    supervisor ! StartReplication

    /*
      config.collections.foreach { c =>
        val router = replicator.actorOf(Props([FetcherActor], c.replicationConfig).withRouter(RoundRobinRouter(nrOfInstances = c.num)), name = c.name)
        replicator.scheduler.schedule(c.initdelay milliseconds, c.period milliseconds, router, StartReplication)
      }
     */
  }

  def stop = ReplicateSystem.shutdown

}
