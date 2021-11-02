/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.bars

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration

@Deprecated
object ValidateBankDetailsRequestsV2 extends ServicesConfiguration {

  val baseUrl = s"${baseUrlFor("bank-account-reputation-frontend")}/bank-account-reputation-frontend"
  val csrfPattern = """<input type="hidden" name="csrfToken" value="([^"]+)""""

  val navigateToHomePage: HttpRequestBuilder =
    http("Navigate to Home Page")
      .get(s"$baseUrl/")
      .check(regex(_ => csrfPattern).saveAs("csrfToken"))
      .check(status.is(200))


  val validateBankDetailsV2: HttpRequestBuilder = {
    http("Submit sort code and account number")
      .post(s"$baseUrl/v2/validateBankDetails": String)
      .formParam("csrfToken", "${csrfToken}")
      .formParam("sortCode", "${sortCode}")
      .formParam("accountNumber", "71201948")
      .check(status.is(200))
      .check(substring("${sortCode}"))
      .check(substring("71201948"))
  }
}
