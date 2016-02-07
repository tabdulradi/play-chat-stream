package playstream.echo

import play.api.mvc._
import play.api.routing.Router
import play.api.routing.sird._

object EchoService {
  val routes: Router.Routes = {
    case GET(p"/$x") => Action {
      Results.Ok(s"Echo: $x")
    }
  }
}