package com.nico.tcp.handlers

import scala.util.Try

sealed trait StartCommand

object StartCommand {

  implicit class StringOps(s: String) {

    def toCmd(): StartCommand =
      if (s.startsWith("start:")) {
        makeInt(s.split(":")(1)).map(StartWithTime).getOrElse(InvalidStart)
      } else {
        InvalidStart
      }

    private def makeInt(s: String) = Try {
      s.toInt
    }
  }
  
  case class StartWithTime(rate: Int) extends StartCommand

  case object InvalidStart extends StartCommand
}



