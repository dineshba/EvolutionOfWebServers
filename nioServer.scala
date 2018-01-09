package httpserver

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class NIOServerSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:8081") // Here is the root for all relative URLs

  val scn = scenario("All get call")
    .exec(http("calls")
      .get("/"))

  setUp(scn.inject(atOnceUsers(10)).protocols(httpConf))
//  setUp(scn.users(100).ramp(10)).protocols(httpConf))
}
