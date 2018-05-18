package com.nico.tcp

sealed trait StreamerName

object StreamerName {
  def apply(name: String): StreamerName = selector.get(name) match {
    case None       =>  throw new Exception("Streamer name not found")
    case Some(name) =>  name
  }

  private val selector = Map(
    "rn" -> RandomNumbers,
    "rs" -> RandomSales
  )

  case object RandomNumbers extends StreamerName
  case object RandomSales extends StreamerName
}