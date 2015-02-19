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
