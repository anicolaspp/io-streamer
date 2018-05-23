package com.nico.tcp.streamers

import akka.util.ByteString

trait DataEncoder[A] {
  def encode(data: A): ByteString = ByteString(data.toString)
}
