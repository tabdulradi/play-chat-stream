package playstream.echo

import play.core.server._
import play.api.routing.sird._
import play.api.mvc._

import playstream.api.Service

class EchoService extends Service {
  lazy val server = NettyServer.fromRouter() {
    case GET(p"/echo/$x") => Action {
      Results.Ok(s"Echo: $x")
    }
  }

  def start() = server
  def stop() = server.stop()
}