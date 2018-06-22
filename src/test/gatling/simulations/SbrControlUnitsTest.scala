import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scala.concurrent.duration._

import scala.language.postfixOps

class SbrControlUnitsTest extends Simulation {

  var baseUrl: String = ConfigLoader("baseUrl")
  var numOfConcurrentUsers: Int = ConfigLoader("concurrentUsers") toInt
  var getRequest = ConfigLoader("get_request")
  var requestName: String = ConfigLoader("request_name_prefix") + getRequest

  println(s"Running test with numOfConcurrentUsers: $numOfConcurrentUsers, baseUrl : $baseUrl, GET Request : $getRequest")

  val httpProtocol: HttpProtocolBuilder = http
    .baseURL(baseUrl)
    .inferHtmlResources()
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-GB,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:60.0) Gecko/20100101 Firefox/60.0")


  val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")
  val scn: ScenarioBuilder = scenario("SbrControlUnitsTest")
    .exec(http(requestName)
      .get(getRequest)
      .headers(headers_0))


  //  setUp(scn.inject(atOnceUsers(numOfConcurrentUsers))).protocols(httpProtocol)

  setUp(scn.inject(constantUsersPerSec(numOfConcurrentUsers) during (1 second)))
    .throttle(jumpToRps(numOfConcurrentUsers), holdFor(1 minute))
    .protocols(httpProtocol)

}
