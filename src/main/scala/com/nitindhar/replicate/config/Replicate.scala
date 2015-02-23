package com.nitindhar.replicate.config

class Replicate(containerMapping: ContainerMapping,
                checkpointMapping: CheckpointMapping,
                fieldMappings: List[FieldMapping])

case class ContainerMapping(source: String, destination: String)

case class CheckpointMapping(name: String, kind: String)

case class FieldMapping(kind: String, source: String, destination: String)
