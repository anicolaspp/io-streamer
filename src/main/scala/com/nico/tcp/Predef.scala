package com.nico.tcp

import akka.Done
import akka.actor.ActorSystem

object Predef {

  implicit class Termination(option: Option[Done]) {
    def terminateActorSystem()(implicit system: ActorSystem) = system.terminate()
  }

  lazy val  __ticks__ = "__TICKS__"
}
