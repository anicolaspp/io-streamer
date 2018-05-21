/**
  * Created by anicolaspp on 5/6/16.
  */

package com.nico.tcp

import akka.actor._
import com.nico.tcp.Predef._

object app {

  implicit val system = ActorSystem("streamer-server")

  def main(args: Array[String]): Unit =
    Configuration
      .parse(args)
      .map(run)
      .terminate()
  
  def run(config: Configuration)(implicit system: ActorSystem): Unit =
    Commander
      .props(config.port, config.name)
      .map(system.actorOf)
      .foreach(commander => {
        commander ! Commander.StartServer
        scala.io.StdIn.readLine()
      })
}