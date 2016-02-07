package playstream.api.play

import play.api.routing.{ SimpleRouter, Router }

class CompositeRouter(routers: Seq[Router]) extends SimpleRouter {
  override def documentation: Seq[(String, String, String)] =
    routers.flatMap(_.documentation)

  override def routes: Router.Routes =
    routers.map(_.routes).fold(Router.empty.routes)(_ orElse _)
}

object CompositeRouter {
  def fromPrefixedRoutes(routers: Seq[(String, Router.Routes)]): CompositeRouter =
    new CompositeRouter(routers.map {
      case (prefix, routes) =>
        Router.from(routes).withPrefix(prefix)
    })
}