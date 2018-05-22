package com.nico.tcp

trait DataSupplier[A] {
  def get(): A
}
