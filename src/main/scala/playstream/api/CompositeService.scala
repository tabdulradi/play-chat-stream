package playstream.api

class CompositeService(services: Service*) extends Service {
  def start() =
    for (service <- services)
      service.start()

  def stop() =
    for (service <- services)
      service.start()
}
