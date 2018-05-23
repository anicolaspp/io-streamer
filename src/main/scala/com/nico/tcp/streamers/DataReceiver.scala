package com.nico.tcp.streamers

import akka.actor.ActorContext
import akka.io.Tcp
import akka.util.ByteString
import com.nico.tcp.Predef._
import com.nico.tcp.handlers.StartCommand._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

trait DataReceiver {
  
  def startConnection(data: ByteString)(implicit context: ActorContext) = {
    data.utf8String.trim.toCmd match {
      case InvalidStart => context.sender ! Tcp.Close
      case StartWithTime(rate) =>
        context
          .system
          .scheduler
          .schedule(1 seconds, rate milliseconds, context.self, (__ticks__, context.sender))
    }
  }
}
