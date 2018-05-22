package com.nico.tcp

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}

import scala.util.Random

class StringStreamer(remote: InetSocketAddress, connection: ActorRef)
  extends Actor
    with ActorStreamer[String] {
    override def receive: Receive = customReceiver(remote, connection)

    override def get(): String = Random.nextString(10)
}

object StringStreamer {
    def props(remote: InetSocketAddress, actor: ActorRef): Props = Props(new StringStreamer(remote, actor))
}