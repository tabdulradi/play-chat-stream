package playstream.main

import akka.actor.{ Props, ActorRef, Actor, ActorSystem }
import akka.cluster.pubsub.{ DistributedPubSubMediator }

/*
 * Provides helper for running experiments on Console
 */
class ConsoleMain {
  val system = ActorSystem("playchatstream")
  val mediator = system.actorOf(Props(new FakeMediator))

}

class FakeMediator extends Actor {
  override def receive: Receive = {
    case msg @ DistributedPubSubMediator.Subscribe(topic, _, _) =>
      getOrCreateTopicActor(topic) forward msg
  }

  def getOrCreateTopicActor(topic: String) =
    context.child(topic).getOrElse(context.actorOf(Props(new FakeTopic), topic))
}

class FakeTopic extends Actor {
  var subscribers = Set.empty[ActorRef]
  override def receive: Receive = {
    case DistributedPubSubMediator.Subscribe(_, _, ref) =>
      subscribers += ref
    case DistributedPubSubMediator.Unsubscribe(_, _, ref) =>
      subscribers -= ref
    case DistributedPubSubMediator.Publish(_, msg, _) =>
      subscribers.foreach(_ forward msg)
  }
}