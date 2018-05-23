package com.nico.tcp.streamers

import java.net.InetSocketAddress

import akka.actor._
import akka.util.ByteString
import com.nico.tcp.streamers.SalesStreamer.{Item, Sale}

import scala.util.Random

class SalesStreamer(remote: InetSocketAddress, connection: ActorRef)
  extends Actor
    with ActorStreamer[Sale] {
    
    context.watch(connection)

    override def receive: Receive = customReceiver(remote, connection)

    override def get(): Sale = Sale(randomId, Item(randomId, randomPrice), java.time.LocalDate.now)

    override def encode(data: Sale): ByteString = ByteString(s"{sale: {id: ${data.id}, item: {itemId: ${data.item.id}, price: ${data.item.price}}, time: ${data.time.toString}}}\n")

    private def randomId = Random.nextInt(100000).toString

    private def randomPrice = Random.nextDouble() * 100
}

object SalesStreamer {
    def props(remote: InetSocketAddress, actor: ActorRef): Props = Props(new SalesStreamer(remote, actor))

    case class Sale(id: String, item: Item, time: java.time.LocalDate)

    case class Item(id: String, price: Double)
}