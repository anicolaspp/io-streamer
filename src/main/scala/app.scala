/**
  * Created by anicolaspp on 5/6/16.
  */

package com.nico.tcp

import akka.actor._

object app {

  def main(args: Array[String]) {

    val system = ActorSystem("streamer-server")

    val port = args(0).toInt
    val streamerName = args(1)

    val commander = system.actorOf(Commander.props(port, streamerName))
    commander ! Commander.StartServer

    scala.io.StdIn.readLine()

    system.terminate()
  }
}