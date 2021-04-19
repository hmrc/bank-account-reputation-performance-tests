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

object AssessBusinessDetailsRequests extends ServicesConfiguration {

  val baseUrl = s"${baseUrlFor("bank-account-reputation")}/bank-account-reputation"
  val csrfPattern = """<input type="hidden" name="csrfToken" value="([^"]+)""""

  val assessBusinessBankDetails: HttpRequestBuilder = {
    http("Submit sort code, account number, name and post code")
      .post(s"$baseUrl/business/v2/assess": String)
      .header("Content-Type", "application/json")
      .body(StringBody(
        """|{
           |  "account": {
           |    "sortCode": "${sortCode}",
           |    "accountNumber": "${accountNumber}"
           |  },
           |  "business": {
           |    "companyName": "${name}",
           |    "companyRegistrationNumber": "UK27318156",
           |    "address": {
           |      "lines": ["22303 Darwin Turnpike"],
           |      "postcode": "${postcode}"
           |    }
           |  }
           |}
           |""".stripMargin)).asJSON
      .check(jsonPath("$.accountExists").is("${accountExists}"))
      .check(status.is(200))
  }
}
