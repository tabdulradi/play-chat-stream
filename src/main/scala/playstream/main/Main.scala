package playstream.main

import playstream.echo.EchoService

object Main {
  def main(args: Array[String]): Unit = {
    val app = new EchoService
    app.start()
  }
}