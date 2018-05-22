package com.nico.tcp

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import akka.util.ByteString

import scala.util.Random

class StringStreamer(remote: InetSocketAddress, connection: ActorRef)
  extends Actor
    with ActorStreamer[String] {

    context.watch(connection)

    override def receive: Receive = customReceiver(remote, connection)

    override def get(): String = Random.nextString(10)

    override def encode(data: String): ByteString = ByteString(data + "\n")
}

object StringStreamer {
    def props(remote: InetSocketAddress, actor: ActorRef): Props = Props(new StringStreamer(remote, actor))
}