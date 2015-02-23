package com.nitindhar.replicate.config

class ReplicatorConfig(source: Storage,
                       destination: Storage,
                       fetcherConfig: FetcherConfig,
                       writerConfig: WriterConfig,
                       replicate: List[Replicate])

class FetcherConfig(batch: Int)

class WriterConfig(workers: Int)
