package uk.gov.hmrc.perftests.example

import uk.gov.hmrc.performance.conf.ServicesConfiguration
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object ValidateBankDetailsRequests extends ServicesConfiguration {

  val baseUrl = s"${baseUrlFor("bank-account-reputation-frontend")}/bank-account-reputation-frontend"
  val csrfPattern = """<input type="hidden" name="csrfToken" value="([^"]+)""""
  val sortCodesFeeder = csv("data/allSortCodes.csv")

  val navigateToHomePage =
    http("Navigate to Home Page")
      .get(s"$baseUrl/")
      .check(regex(_ => csrfPattern).saveAs("csrfToken"))
      .check(status.is(200))


  val validateBankDetails = {
    http("Submit sort code and account number")
      .post(s"$baseUrl/validateBankDetails": String)
      .formParam("csrfToken", "${csrfToken}")
      .formParam("sortCode", "${sortCode}")
      .formParam("accountNumber", "71201948")
      .check(status.is(200))
      .check(substring("${sortCode}"))
      .check(substring("71201948"))
  }
}
