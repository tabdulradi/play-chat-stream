package playstream.main

import akka.actor.ActorSystem
import akka.cluster.pubsub.DistributedPubSub
import com.typesafe.config.ConfigFactory
import playstream.api.CompositeService
import playstream.api.play.PlayService
import playstream.chat.ChatService
import playstream.echo.EchoService

object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("playchatstream", ConfigFactory.load("main.conf"))
    val mediator = DistributedPubSub(system).mediator

    val playService = new PlayService(
      "/echo" -> EchoService.routes,
      "/chat" -> new ChatService(mediator).routes
    )

    val app = new CompositeService(playService)
    app.start()
  }
}