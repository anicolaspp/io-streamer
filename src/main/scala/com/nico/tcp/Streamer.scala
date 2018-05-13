/**
  * Created by anicolaspp on 6/8/16.
  */
package com.nico.tcp

import java.net.InetSocketAddress
import akka.actor._
import akka.io.Tcp
import akka.util.ByteString
import scala.util.Random


class Streamer(remote: InetSocketAddress, connection: ActorRef) extends Actor with ActorLogging {
  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global

  val __ticks__ = "__TICKS__"
  context.watch(connection)

  override def receive: Receive = {
    case Tcp.Received(data) => {
      data.utf8String.trim match {
        case initCmd if initCmd.startsWith("start:")  => context.system.scheduler.schedule(1 seconds, initCmd.split(":")(1).toInt milliseconds, self, (__ticks__, sender))
        case _        => sender ! Tcp.Close
      }
    }

    case (`__ticks__`, s: ActorRef) => self ! Streamer.Send(data = Random.nextInt().toString(), to = s)

    case Streamer.Send(data, to) => log.info(data)
      to ! Tcp.Write(ByteString(data + "\n"))

    case Terminated(`connection`) => log.debug(s"The remote address $remote has died")
      context.stop(self)
  }
}

object Streamer {
  def props(remote: InetSocketAddress, actor: ActorRef): Props = Props(new Streamer(remote, actor))

  case class Send(data: String, to: ActorRef)
}