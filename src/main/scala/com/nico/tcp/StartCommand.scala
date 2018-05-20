package com.nico.tcp

import scala.util.Try

sealed trait StartCommand

case class StartWithtTime(rate: Int) extends StartCommand

case object InvalidStart extends StartCommand

object StartCommand {

  implicit class toStartCommand(s: String) {
    def toCmd(): StartCommand =
      if (s.startsWith("start:")) {
        makeInt(s.split(":")(1))
          .map(rate => StartWithtTime(rate))
          .getOrElse(InvalidStart)
      } else {
        InvalidStart
      }
  }

  private def makeInt(s: String) = Try {
    s.toInt
  }
}



