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

    runCommander(port, streamerName)
    
    scala.io.StdIn.readLine()

    system.terminate()
  }

  def runCommander(port: Int, name: String)(implicit system: ActorSystem) = Commander.props(port, name) match {
    case None         =>  println(s"Streamer ${name} not found")
    case Some(props)  =>  system.actorOf(props) ! Commander.StartServer
  }
}