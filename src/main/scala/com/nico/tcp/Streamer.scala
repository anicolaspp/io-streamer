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

  override def receive: Actor.Receive = {
    case Tcp.Received(data) => {
      data.utf8String.trim match {
        case "start" => context.system.scheduler.schedule(1 seconds, 200 milliseconds, self, (__ticks__, sender))
        case "stop" => sender ! Tcp.Close
      }
    }

    case (__ticks__, s: ActorRef) => self ! Streamer.Send(Random.nextString(10), s)

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
