import cats.effect.IO

import java.io.PrintWriter
import scala.io.Source

object ResponseGenerator extends App {

  val r = new scala.util.Random().alphanumeric
  //generate random payload of size [40MB] and write it to a file
  writeToFile(apiResult(r.take(40000000).mkString("")), "/Users/mukhayyo.tashpulatov/mycode/scala-http4s-decoder/src/main/scala/response.json")

  //MY JSON RESPONSE
  def apiResult(content: String): String =
    s"""{
       | "name": "test http4s decoder",
       | "payload": "$content"
       | }""".stripMargin

  def writeToFile(contents: String, path: String) = {
    println(s"Writing generated test prime response file to path: $path")
    new PrintWriter(path) {
      write(contents)
      close()
    }
  }

  //GET CONTENT FROM THE FILE
  def getContent(filename: String): String = {
    IO(Source.fromFile(filename))
      .bracketCase(source => IO(source.mkString.stripMargin))((source, _) => IO(source.close()))
      .unsafeRunSync()
  }

}