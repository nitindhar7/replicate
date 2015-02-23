package com.nitindhar.replicate

trait Writer {
  val workers: Int

  def write[A](batch: List[A]): Unit
}

trait PostgreSqlWriter extends Writer
