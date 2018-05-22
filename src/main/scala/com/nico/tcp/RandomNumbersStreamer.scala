/**
  * Created by anicolaspp on 6/8/16.
  */
package com.nico.tcp

import java.net.InetSocketAddress

import akka.actor._
import akka.io.Tcp
import akka.util.ByteString
import com.nico.tcp.Ticks._

import scala.util.Random

class RandomNumbersStreamer(remote: InetSocketAddress, connection: ActorRef) extends Actor with ActorLogging with DataReceiver {

  context.watch(connection)

  override def receive: Receive = {
    case Tcp.Received(data) => handle(data)

    case (`__ticks__`, s: ActorRef) => self ! RandomNumbersStreamer.Send(data = Random.nextInt().toString, to = s)

    case RandomNumbersStreamer.Send(data, to) => log.info(data)
      to ! Tcp.Write(ByteString(data + "\n"))

    case Terminated(`connection`) => log.debug(s"The remote address $remote has died")
      context.stop(self)
  }
}

object RandomNumbersStreamer {
  def props(remote: InetSocketAddress, actor: ActorRef): Props = Props(new RandomNumbersStreamer(remote, actor))

  case class Send(data: String, to: ActorRef)
}