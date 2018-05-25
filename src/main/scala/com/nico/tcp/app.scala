/**
  * Created by anicolaspp on 5/6/16.
  */

package com.nico.tcp

import akka.Done
import akka.actor._
import com.nico.tcp.Predef._
import com.nico.tcp.conf.Configuration
import com.nico.tcp.handlers.Commander

object app {

  implicit val system: ActorSystem = ActorSystem("streamer-server")

  def main(args: Array[String]): Unit =
    Configuration
      .parse(args)
      .map(run)
      .terminateActorSystem()

  def run(config: Configuration)(implicit system: ActorSystem): Done =
    Commander
      .props(config.port, config.name)
      .map(system.actorOf)
      .map(commander => {
        commander ! Commander.StartServer
        scala.io.StdIn.readLine()
        Done
      })
      .getOrElse(Done)
}