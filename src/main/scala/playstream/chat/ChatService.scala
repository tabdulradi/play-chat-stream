package playstream.chat

import akka.actor.ActorRef
import akka.stream.OverflowStrategy
import akka.stream.scaladsl._
import akka.cluster.pubsub.DistributedPubSubMediator
import play.api.mvc._
import play.api.routing.Router
import play.api.routing.sird._

class ChatService(mediator: ActorRef) {
  val routes: Router.Routes = {
    case GET(p"/$room") => Action {
      Results.Ok(html.chat.render(room))
    }
    case GET(p"/$room/ws") => WebSocket.accept[String, String] { request =>
      ChatService.topicFlow[String](mediator, room)
    }
  }
}

object ChatService {
  def fromMediator[Out](mediator: ActorRef, topic: String): Source[Out, ActorRef] =
    Source
      .actorRef[Out](10, OverflowStrategy.dropHead)
      .mapMaterializedValue { ref => mediator ! DistributedPubSubMediator.Subscribe(topic, ref); ref }

  def wrapWithPublish[T](topic: String): Flow[T, DistributedPubSubMediator.Publish, Unit] =
    Flow[T].map(DistributedPubSubMediator.Publish(topic, _))

  def toMediator(mediator: ActorRef): Sink[DistributedPubSubMediator.Publish, Unit] =
    Sink.actorRef[DistributedPubSubMediator.Publish](mediator, ()) // http://stackoverflow.com/questions/35255422/create-sink-based-on-materialised-value-of-a-source

  def publishToMediator[In](mediator: ActorRef, topic: String): Sink[In, Unit] =
    wrapWithPublish(topic) to toMediator(mediator)

  def topicFlow[M](mediator: ActorRef, topic: String): Flow[M, M, Unit] =
    Flow.fromSinkAndSource(
      publishToMediator(mediator, topic),
      fromMediator(mediator, topic))
}