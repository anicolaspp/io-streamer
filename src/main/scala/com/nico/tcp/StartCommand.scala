package com.nico.tcp

import scala.util.Try

sealed trait StartCommand

object StartCommand {

  implicit class toStartCommand(s: String) {
    def toCmd(): StartCommand =
      if (s.startsWith("start:")) {
        makeInt(s.split(":")(1))
          .map(StartWithTime)
          .getOrElse(InvalidStart)
      } else {
        InvalidStart
      }
  }

  private def makeInt(s: String) = Try {
    s.toInt
  }

  case class StartWithTime(rate: Int) extends StartCommand

  case object InvalidStart extends StartCommand

}



