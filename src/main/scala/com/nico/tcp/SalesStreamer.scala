package com.nico.tcp

import java.net.InetSocketAddress

import akka.actor._
import akka.io.Tcp
import akka.util.ByteString
import com.nico.tcp.SalesStreamer._
import com.nico.tcp.Ticks._

import scala.util.Random

class SalesStreamer(remote: InetSocketAddress, connection: ActorRef) extends Actor with DataReceiver {
    
    context.watch(connection)

    override def receive: Receive = {
        case Tcp.Received(data) => handle(data)

        case Terminated(`connection`)   =>  context.stop(self)

        case (`__ticks__`, s: ActorRef) =>  self ! (generateRandomSale(), s)

        case (data: Sale, to: ActorRef) =>  to ! Tcp.Write(ByteString(encode(data) + "\n"))
    }

    private def generateRandomSale() = Sale(randomId, Item(randomId, randomPrice), java.time.LocalDate.now)

    private def encode(sale: Sale) = s"{sale: {id: ${sale.id}, item: {itemId: ${sale.item.id}, price: ${sale.item.price}}, time: ${sale.time.toString}}}"
    
    private def randomId = Random.nextInt(100000).toString

    private def randomPrice = Random.nextDouble() * 100
}

object SalesStreamer {
    def props(remote: InetSocketAddress, actor: ActorRef): Props = Props(new SalesStreamer(remote, actor))

    case class Sale(id: String, item: Item, time: java.time.LocalDate)

    case class Item(id: String, price: Double)
}
