/**
  * Created by anicolaspp on 5/6/16.
  */

package com.nico.tcp

import akka.actor._

object app {

  def main(args: Array[String]) {

    val system = ActorSystem("echo-server")

    val commander = system.actorOf(Commander.props(args(0).toInt))
    commander ! Commander.StartServer

    scala.io.StdIn.readLine()

    system.terminate()
  }
}









