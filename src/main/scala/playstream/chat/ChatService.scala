package playstream.chat

import akka.stream.scaladsl.Flow
import play.api.mvc._
import play.api.routing.Router
import play.api.routing.sird._

object ChatService {
  val routes: Router.Routes = {
    case GET(p"/$room") => WebSocket.accept[String, String] { request =>
      Flow[String].map(s"$room: " + _)
    }
  }
}