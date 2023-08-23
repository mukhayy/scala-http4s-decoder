import ResponseGenerator.getContent
import cats.effect.{ContextShift, IO, Timer}
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, get, urlEqualTo}
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf
import io.circe.generic.auto._
import org.http4s.ember.client.EmberClientBuilder
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.ExecutionContext

class http4sResponseTest extends AnyFlatSpec with BeforeAndAfterAll {

  //MY MODEL
  case class Response(name:String, payload: String)

  "Response" should "be handled in reasonable time" in {

    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
    implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)
    implicit val responseDecoder: EntityDecoder[IO, Response] = jsonOf[IO, Response]

    val wiremockConf: WireMockConfiguration = options().port(80)
    val wireMockServer: WireMockServer = new WireMockServer(wiremockConf)

    wireMockServer.start()
    wireMockServer.stubFor(
      get(urlEqualTo(s"/myTestEndpoint"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withBody(
              getContent("src/main/scala/response.json")
            )
        )
    )

    //TIME STARTED
    val int = System.currentTimeMillis()

    EmberClientBuilder
      .default[IO]
      .build
      .use { client =>
        client.get("http://localhost:80/myTestEndpoint") { response =>
          for {
            /* Handling response as String and then parsing it to custom object Response */
            //str <- response.as[String]
            //myResponse <- IO(io.circe.parser.decode[Response](str))

            /* Handling response as custom object Response */
            myResponse <- response.as[Response]
          } yield myResponse
        }
      }.unsafeRunSync()

    //TIME FINISHED
    println(s"It took this much time: ${System.currentTimeMillis() - int}")
    wireMockServer.stop()
  }
}