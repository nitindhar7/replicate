package com.nitindhar.replicate

import akka.actor.{Props, ActorSystem}
import com.nitindhar.replicate.Messages.StartReplication

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

  def start = ReplicateSystem.actorOf(Props[ReplicationSupervisor]) ! StartReplication

  def stop = ReplicateSystem.shutdown

}
