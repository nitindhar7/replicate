package com.nitindhar.replicate

trait Fetcher {
  val batch: Int

  def fetch[A, B](checkpoint: A): List[B]
}

trait MongoDbFetcher extends Fetcher
