package com.nico.tcp

import java.net.InetSocketAddress

import akka.actor.{ActorContext, ActorRef, Terminated}
import akka.event.LoggingReceive
import akka.io.Tcp
import akka.util.ByteString
import com.nico.tcp.Predef._

trait ActorStreamer[A] extends DataReceiver with DataSupplier[A] with DataEncoder[A] {

  def customReceiver(remote: InetSocketAddress, connection: ActorRef)(implicit context: ActorContext) = LoggingReceive {
    case Tcp.Received(data)           => startConnection(data)
    case (`__ticks__`, s: ActorRef)   => context.self ! ActorStreamer.Send(encode(get()), s)
    case ActorStreamer.Send(data, to) => to ! Tcp.Write(data)
    case Terminated(`connection`)     => context.stop(context.self)
  }
}

object ActorStreamer {
  case class Send(data: ByteString, to: ActorRef)
}