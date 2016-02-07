package playstream.main

import playstream.api.CompositeService
import playstream.api.play.PlayService
import playstream.chat.ChatService
import playstream.echo.EchoService
import playstream.hello.HelloWorldService

object Main {
  def main(args: Array[String]): Unit = {
    val playService = new PlayService(
      "/echo" -> EchoService.routes,
      "/chat" -> ChatService.routes
    )
    val app = new CompositeService(playService, new HelloWorldService)
    app.start()
  }
}