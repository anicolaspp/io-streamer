package com.nico.tcp

sealed trait StreamerName

object StreamerName {
  def apply(name: String): Option[StreamerName] = selector.get(name)

  private val selector = Map(
    "rn" -> RandomNumbers,
    "rs" -> RandomSales
  )

  case object RandomNumbers extends StreamerName
  case object RandomSales extends StreamerName
}