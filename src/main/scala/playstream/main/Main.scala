package playstream.main

import playstream.hello.HelloWorldService

object Main {
  def main(args: Array[String]): Unit = {
    val app = new HelloWorldService
    app.start()
    app.stop()
  }
}