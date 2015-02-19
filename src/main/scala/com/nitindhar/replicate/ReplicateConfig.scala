package com.nitindhar.replicate

case class ReplicateConfig(readerConfig: ReaderConfig,
                           writerConfig: WriterConfig,
                           queueConfig: QueueConfig)

object ReplicateConfig {

  def apply(numOfReaders: Int,
            initialReaderPeriod: Int,
            readerFrequency: Int,
            numOfWriters: Int,
            initialWriterPeriod: Int,
            writerFrequency: Int): ReplicateConfig = {
    ReplicateConfig(
      ReaderConfig(numOfReaders, initialReaderPeriod, readerFrequency),
      WriterConfig(numOfWriters, initialWriterPeriod, writerFrequency),
      QueueConfig()
    )
  }

}

case class ReaderConfig(numOfReaders: Int,
                        initialPeriod: Int,
                        frequency: Int)

case class WriterConfig(numOfWriters: Int,
                        initialPeriod: Int,
                        frequency: Int)

case class QueueConfig()
