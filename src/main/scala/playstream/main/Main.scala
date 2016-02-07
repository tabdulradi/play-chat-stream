package playstream.main

import playstream.api.CompositeService
import playstream.echo.EchoService
import playstream.hello.HelloWorldService

object Main {
  def main(args: Array[String]): Unit = {
    val app = new CompositeService(new EchoService, new HelloWorldService)
    app.start()
  }
}