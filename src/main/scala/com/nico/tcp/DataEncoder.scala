package com.nico.tcp

import akka.util.ByteString

trait DataEncoder[A] {
  def encode(data: A): ByteString = ByteString(data.toString)
}
