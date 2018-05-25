/**
  * Created by anicolaspp on 6/8/16.
  */

package com.nico.tcp.handlers

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, Props}
import akka.io.{IO, Tcp}
import com.nico.tcp.handlers.Commander.StartServer
import com.nico.tcp.handlers.StreamerName.{Files, RandomNumbers, RandomSales, RandomStrings}
import com.nico.tcp.streamers.{FileStreamer, RandomNumbersStreamer, SalesStreamer, StringStreamer}
import com.typesafe.config.{Config, ConfigFactory}

class Commander(endpoint: InetSocketAddress, streamerName: StreamerName) extends Actor with ActorLogging {

  override def receive: Receive = {
    case StartServer => IO(Tcp)(context.system) ! Tcp.Bind(self, endpoint)
    case Tcp.Connected(remote, _) => {
      log.debug(s"Remote address $remote connected")
      log.debug(s"Starting streamer: $streamerName...")

      val streamer = toActor(propsFor(remote))


      sender ! Tcp.Register(streamer)
    }
  }
  
  private def propsFor(remote: InetSocketAddress) = streamerName match {
    case RandomNumbers  =>  RandomNumbersStreamer.props(remote, sender)
    case RandomSales    =>  SalesStreamer.props(remote, sender)
    case RandomStrings  =>  StringStreamer.props(remote, sender)
    case Files          =>  FileStreamer.props(remote, sender, ConfigFactory.load().getString("folder"))
  }

  private def toActor(props: Props) = context.actorOf(props)
}

object Commander {
  def props(port: Int, name: String): Option[Props] =
    StreamerName(name).map(name => Props(new Commander(new InetSocketAddress("localhost", port), name)))

  case object StartServer
}