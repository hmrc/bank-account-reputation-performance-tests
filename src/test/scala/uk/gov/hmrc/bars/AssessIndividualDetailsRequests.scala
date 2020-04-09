package uk.gov.hmrc.bars

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object AssessIndividualDetailsRequests extends ServicesConfiguration {

  val baseUrl = s"${baseUrlFor("bank-account-reputation")}/bank-account-reputation"
  val csrfPattern = """<input type="hidden" name="csrfToken" value="([^"]+)""""

  val assessIndividualBankDetails: HttpRequestBuilder = {
    http("Submit sort code, account number, name and post code")
      .post(s"$baseUrl/individual/v2/assess": String)
      .header("Content-Type", "application/json")
      .body(StringBody(
        """|{
           |  "account": {
           |    "sortCode": "${sortCode}",
           |    "accountNumber": "${accountNumber}"
           |  },
           |  "subject": {
           |    "name": "${title} ${firstname} ${surname}",
           |    "address": {
           |      "lines": [
           |      "${flatNumber}${streetNumber} ${street}",
           |      "${town}"
           |      ],
           |      "postcode": "${postcode}"
           |    }
           |  }
           |}
           |""".stripMargin)).asJSON
      .check(substring(""""accountExists":"${accountexists}""""))
      .check(status.is(200))
  }
}
