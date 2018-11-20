# replicate
Scala replicator for replicating live data between two data stores

Currently supported:

MongoDb -> PostgreSQL

Data Sources
============

# Reading from sources
    - mongo
        reactive mongo
        which collection to read from?
        for each collection

# Checkpoint
    - where to store this?


replications: [
    {
        key: "users",
        source: {
            db: "mongo",
            collection: "users",
            fields: ["name", "email", "age"],
            sort_by: "created_at",
            batch:
        },
        destination: {
            db: "postgresql",
            collection: "users",
            fields: ["fullname", "email_address", "age"]
            parallelism: 10
        }

    }
]

Replicator.start {

}

======================================================================================================================
STORAGE
======================================================================================================================
trait Storage {
  val config: DatabaseConfig
}

trait MongoDbStorage extends Storage {
  private val driver = new MongoDriver
  private val connection = driver.connection(List(config.host))
  private val db = connection(config.db)

  def collection(collName: String) = db(collName)
}

object MongoDbStorage {
  def apply(mongoDbConfig: MongoDbConfig) = new MongoDbStorage {
    override val config = mongoDbConfig
  }
}

======================================================================================================================
API
======================================================================================================================

val replicator = new Replicator with ReplicatorConfig {
  override val source = ...
  override val destination = ...
  override val fetcher = ...
  override val writer = ...
  override val replicates = ...
}

trait Replicator {
  self: ReplicatorConfig =>

  def start = {

  }

  def stop = {

  }

}

object Replicator extends Replicator with ReplicatorConfig

======================================================================================================================
FETCHERS
======================================================================================================================
trait Fetcher {
  val batch: Int

  def fetch[A, B](checkpoint: A): List[B]
}
trait MongoDbFetcher extends Fetcher

======================================================================================================================
WRITERS
======================================================================================================================
trait Writer {
  val workers: Int

  def write[A](batch: List[A]): Unit
}
trait PostgreSqlWriter extends Writer

======================================================================================================================
CONFIG
======================================================================================================================
trait ReplicatorConfig {
  val source: Storage
  val destination: Storage
  val fetcher: FetcherConfig
  val writer: WriterConfig
  val replicates: List[Replicate]
}

class Storage(kind: String, storageConfig: StorageConfig)
trait StorageConfig {
    val host: String
    val port: String
    val db: String
}
case class MongoDbConfig(host: String, port: String, db: String) extends StorageConfig
case class PostgreSqlConfig(host: String, port: String, db: String) extends StorageConfig

class FetcherConfig(batch: Int)

class WriterConfig(workers: Int)

class Replicate(containerMapping: ContainerMapping, checkpointMapping: CheckpointMapping, fieldMappings: List[FieldMapping])
case class ContainerMapping(source: String, destination: String)
case class CheckpointMapping(name: String, kind: String)
case class FieldMapping(kind: String, source: String, destination: String)

replicator: {
    source: {
        kind: mongodb,
        host: localhost,
        port: 27017,
        db: test
    },
    destination: {
        kind: postgresql,
        host: localhost,
        port: 3301,
        db: test
    },
    fetcher: {
        batch: 1000
    },
    writer: {
        workers: 100
    },
    replicates: [{
        container_mapping: {
            source: users,
            destination: dim_user
        },
        checkpoint_mapping: {
            name: updated_at,
            kind: Date
        },
        field_mappings: [
            {
                kind: String,
                source: name,
                destination: name
            },
            {
                kind: String,
                source: email,
                destination: email
            },
            {
                kind: String,
                source: password,
                destination: password
            }
        ]
    }]
}

======================================================================================================================
EXPERIMENTAL
======================================================================================================================
trait Replicator {

  def replicate[A](checkpoint: A): Boolean

}

class Replicator(fetcher: Fetcher, writerRouter: ActorRef) extends Replicator {

  override def replicate[A](checkpoint: A) = {
    fetcher.fetch(checkpoint) map { replicatees =>
      replicatees foreach { r =>
        writerRouter ! r
      }
    }
  }

}

new Replicator(
  Fetcher.fromConfig(... pass in some config ...),
  someRouter
)

object Messages {
  case object StartReplication
  case class Replicable[A](replicatees: List[A])
  case object Replicated
}

/**
 * Replicate.start
 */
object Replicate {
  import Messages._

  private val system = ActorSystem(...)

  def start(config: Config) = {
    config.replicatorConfigs.foreach { subconf =>
      val replicationActor = Actor.withRouter(worker).withConfig(subconf)
      system.scheduler.schedule(0 milliseconds, 50 milliseconds, replicationActor, StartReplication)
    }
  }

}

class ReplicationActor(config: SubConfig) extends Actor with ActorLogging {

  val fetcher = Fetcher.fromConfig(config)

  val storingActor = Actor.withRouter(worker).withConfig(config)

  def receive = {
    case StartReplication => storingActor ! Replicable(fetcher.fetch(ControlFetcher.checkpoint))
    case Replicated => log.info("Replicated data!")
  }

}

class StoringActor(config: SubConfig) extends Actor with ActorLogging {

  val writer = Writer.fromConfig(config)

  def receive = {
    case Replicable(replicatees) => replicatees foreach { r =>
      writer(r)
      sender ! Replicated
    }
  }

}


