package playstream.hello

import playstream.api.Service

class HelloWorldService extends Service {
  def start() = println("Hello world")
  def stop() = println("Goodbye")
}