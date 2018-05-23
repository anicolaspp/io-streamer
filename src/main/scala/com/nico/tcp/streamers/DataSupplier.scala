package com.nico.tcp.streamers

trait DataSupplier[A] {
  def get(): A
}
