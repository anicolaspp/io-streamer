/**
  * Created by anicolaspp on 6/8/16.
  */

package com.nico.tcp

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, Props}
import akka.io.{IO, Tcp}


class Commander(endpoint: InetSocketAddress, streamerName: StreamerName) extends Actor with ActorLogging {
  import Commander._
  import StreamerName._
  
  override def receive: Receive = {
    case StartServer                =>  IO(Tcp)(context.system) ! Tcp.Bind(self, endpoint)
    case Tcp.Connected(remote, _)   =>  {
      log.debug(s"Remote address $remote connected")
      log.debug(s"Starting streamer: ${streamerName}...")

      val selectedStreamerProps = getStreamerProps(remote)

      sender ! Tcp.Register(context.actorOf(selectedStreamerProps))
    }
  }

  private def getStreamerProps(remote: InetSocketAddress) = streamerName match {
    case RandomNumbers => Streamer.props(remote, sender)
    case RandomSales   => SalesStreamer.props(remote, sender)
  }
}

object Commander {
  def props(port: Int, name: String): Props = Props(new Commander(new InetSocketAddress("localhost", port), StreamerName(name)))

  case class StartServer()
}

sealed trait StreamerName

object StreamerName {
  def apply(name: String): StreamerName = selector.get(name) match {
    case None       =>  throw new Exception("Streamer name not found")
    case Some(name) =>  name
  }

  private val selector = Map(
    "rn" -> RandomNumbers,
    "rs" -> RandomSales
    )

  case object RandomNumbers extends StreamerName
  case object RandomSales extends StreamerName
}
 