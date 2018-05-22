/**
  * Created by anicolaspp on 6/8/16.
  */
package com.nico.tcp

import java.net.InetSocketAddress

import akka.actor._

import scala.util.Random

class RandomNumbersStreamer(remote: InetSocketAddress, connection: ActorRef)
  extends Actor
    with ActorLogging
    with ActorStreamer[Int] {

  context.watch(connection)

  override def receive: Receive = customReceiver(remote, connection)

  override def get(): Int = Random.nextInt()
}

object RandomNumbersStreamer {
  def props(remote: InetSocketAddress, actor: ActorRef): Props = Props(new RandomNumbersStreamer(remote, actor))
}