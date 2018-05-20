/**
  * Created by anicolaspp on 5/6/16.
  */

package com.nico.tcp

import akka.actor._

object app {

  def main(args: Array[String]) {

    implicit val system = ActorSystem("streamer-server")

    val port = args(0).toInt
    val streamerName = args(1)

    runWith(port, streamerName)

    system.terminate()
  }

  def runWith(port: Int, name: String)(implicit system: ActorSystem): Unit = 
    Commander
      .props(port, name)
      .map(system.actorOf(_))
      .foreach(commander => {
        commander ! Commander.StartServer
        scala.io.StdIn.readLine()
      })
}