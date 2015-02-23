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

override def onStart {
  Replicator.start
}

override def onStop {
  Replicator.stop
}

object Replicator {

  def start = {

  }

  def stop = {

  }

}

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

class ReplicatorConfig(source: Storage,
                       destination: Storage,
                       fetcher: FetcherConfig,
                       writer: WriterConfig,
                       replicates: List[Replicate])

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




