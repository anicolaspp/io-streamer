package com.nico.tcp

import akka.actor.ActorSystem

object Predef {

  implicit class Termination(option: Option[Unit]) {
    def terminateActorSystem()(implicit system: ActorSystem) = system.terminate()
  }
}
