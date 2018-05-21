package com.nico.tcp

import akka.actor.ActorSystem

object Predef {

  implicit class Termination[A](option: Option[A]) {
    def terminate()(implicit system: ActorSystem) = system.terminate()
  }
}
