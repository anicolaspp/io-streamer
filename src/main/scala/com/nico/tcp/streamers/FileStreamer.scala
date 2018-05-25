package com.nico.tcp.streamers

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import akka.util.ByteString
import com.nico.tcp.streamers.FileStreamer.{FileFragment, FinalFragment, Fragment}

import scala.util.Try

class FileStreamer(remote: InetSocketAddress, connection: ActorRef, path: String)
  extends Actor
    with ActorStreamer[Fragment] {

  private val files = readFolderFiles(path)

  private val fragments = files.flatMap(name => splitFile(name)).iterator

  override def receive: Receive = customReceiver(remote, connection)

  override def get(): Fragment = if (fragments.hasNext) fragments.next() else FinalFragment

  override def encode(data: Fragment): ByteString = data match {
    case FileFragment(fileName, fragmentId, content)  => ByteString(s"{path: $fileName, id: $fragmentId, content: $content}")
    case FinalFragment                                => ByteString("{}")
  }

  private def splitFile(name: String): Stream[FileFragment] = {
    io.Source
      .fromFile(name)
      .getLines()
      .toStream
      .zipWithIndex
      .map { case (line, index) => FileFragment(name, index, line)}
  }

  private def readFolderFiles(path: String) = Try {
    new java.io.File(path).list()
  }
    .toOption
    .map(_.toStream).getOrElse(Stream.empty)
}

object FileStreamer {
  def apply(remote: InetSocketAddress, actor: ActorRef, path: String): Props =
    Props(new FileStreamer(remote, actor, path))

  sealed trait Fragment

  case class FileFragment(fileName: String, fragmentId: Int, content: String) extends Fragment
  case object FinalFragment extends Fragment
}