package playstream.chat

import akka.actor.ActorSystem
import akka.cluster.pubsub.DistributedPubSub
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ HttpMethods, HttpRequest }
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Keep, Sink }
import play.api.mvc._
import play.api.routing.Router
import play.api.routing.sird._

class ChatBotsService()(implicit system: ActorSystem) {
  val mediator = DistributedPubSub(system).mediator
  implicit val materializer = ActorMaterializer()

  topicToTwitter("test").run() // Remove me

  def twitterPool =
    Http()
      .cachedHostConnectionPool[Unit]("api.twitter.com")
      .to(Sink.foreach(println))

  def request(msg: String): HttpRequest = // FIXME
    HttpRequest(HttpMethods.POST, uri = s"/1.1/statuses/update.json?status=$msg").withHeaders()

  def topicToTwitter(room: String) =
    ChatService
      .fromMediator[String](mediator, room) // TODO rate limiting
      .map(request)
      .map(_ -> ())
      .toMat(twitterPool)(Keep.none)

  val routes: Router.Routes = {
    case POST(p"/$room/twitter/publisher") => Action {
      topicToTwitter(room).run()
      Results.Created
    }
  }
}

