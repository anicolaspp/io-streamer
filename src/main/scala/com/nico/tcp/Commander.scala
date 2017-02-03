/**
  * Created by anicolaspp on 6/8/16.
  */

package com.nico.tcp

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, Props}
import akka.io.{IO, Tcp}


class Commander(endpoint: InetSocketAddress) extends Actor with ActorLogging {
  import Commander._
  override def receive: Receive = {
    case StartServer   =>    IO(Tcp)(context.system) ! Tcp.Bind(self, endpoint)
    case Tcp.Connected(remote, _)   =>  {
      log.debug(s"Remote address $remote connected")
      sender ! Tcp.Register(context.actorOf(Streamer.props(remote, sender)))
    }
  }
}

object Commander {
  def props(port: Int): Props = Props(new Commander(new InetSocketAddress("localhost", port)))

  case class StartServer()
}
