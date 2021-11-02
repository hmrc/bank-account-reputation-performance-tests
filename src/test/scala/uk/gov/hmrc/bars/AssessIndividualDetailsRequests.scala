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
object AssessIndividualDetailsRequests extends ServicesConfiguration {

  val baseUrl = s"${baseUrlFor("bank-account-reputation")}"
  val csrfPattern = """<input type="hidden" name="csrfToken" value="([^"]+)""""

  val assessIndividualBankDetails: HttpRequestBuilder = {
    http("Submit sort code, account number, name and post code")
      .post(s"$baseUrl/personal/v3/assess": String)
      .header(HttpHeaderNames.ContentType, "application/json")
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
           |""".stripMargin)).asJson
      .check(jsonPath("$.accountExists").is("${accountExists}"))
      .check(status.is(200))
  }
}
