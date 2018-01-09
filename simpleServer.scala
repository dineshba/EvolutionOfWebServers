package httpserver

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class SimpleServerSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:8080")

  val scn = scenario("All get call")
    .exec(http("calls")
      .get("/"))

  setUp(scn.inject(atOnceUsers(20)).protocols(httpConf))
}
