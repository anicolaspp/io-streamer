package com.nico.tcp.conf

import com.nico.tcp.handlers.StreamerName
import scopt.OptionParser

case class Configuration(port: Int, name: String)

object Configuration {

  def parse(args: Seq[String]): Option[Configuration] = parser.parse(args, Configuration.default)

  def default: Configuration = DefaultConfig

  private object DefaultConfig extends Configuration(9090, "rn")

  private val parser = new scopt.OptionParser[Configuration]("IO-Streamer") {
    head("IO-Streamer")

    opt[Int]('p', "port")
      .action((p, config) => config.copy(port = p))
      .required()
      .maxOccurs(1)
      .validate(validatePort)
      .text("Port to bind the server")

    opt[String]('n', "streamer-name")
      .action((name, config) => config.copy(name = name))
      .required()
      .maxOccurs(1)
      .validate(validateName)
      .text("Name of the streamer to be used")

    private def validatePort(port: Int) =
      if (port > 0) success else failure("Port number must be greater than zero (0)")

    private def validateName(name: String) =
      StreamerName
        .apply(name)
        .map(_ => success)
        .getOrElse(failure(s"Possible names: ${StreamerName.names.mkString(",")}"))
  }
}