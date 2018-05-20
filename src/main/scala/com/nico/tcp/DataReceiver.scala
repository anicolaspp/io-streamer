package com.nico.tcp

import akka.actor.ActorContext
import akka.io.Tcp
import akka.util.ByteString
import scala.concurrent.duration._

trait DataReceiver {

  import StartCommand._
  import Ticks._

  import scala.concurrent.ExecutionContext.Implicits.global


  def handle(data: ByteString)(implicit context: ActorContext) = {
    data.utf8String.trim.toCmd match {
      case InvalidStart => context.sender ! Tcp.Close
      case StartWithtTime(rate) => context.system.scheduler.schedule(1 seconds, rate milliseconds, context.self, (__ticks__, context.sender))
    }
  }
}
