package playstream.api.play

import play.api.BuiltInComponents
import play.api.routing.Router
import play.core.server.NettyServerComponents
import playstream.api.Service

class PlayService(prefixedRoutes: (String, Router.Routes)*) extends Service {

  val components = new NettyServerComponents with BuiltInComponents {
    lazy val router = CompositeRouter.fromPrefixedRoutes(prefixedRoutes)
  }

  def start() =
    components.server

  def stop() =
    components.server.stop()
}
