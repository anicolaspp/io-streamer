package com.nico.tcp.handlers

sealed trait StreamerName

object StreamerName {
  def apply(name: String): Option[StreamerName] = selector.get(name)
  
  def names = selector.keys.toList

  private lazy val selector = Map(
    "rn" -> RandomNumbers,
    "rs" -> RandomSales,
    "rstr" -> RandomStrings,
    "fs"  -> Files
  )

  case object RandomNumbers extends StreamerName
  case object RandomSales extends StreamerName
  case object RandomStrings extends StreamerName
  case object Files extends StreamerName
}