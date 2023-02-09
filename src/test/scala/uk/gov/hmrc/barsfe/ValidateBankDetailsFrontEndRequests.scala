/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.barsfe

import java.net.URLDecoder
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import io.gatling.http.HeaderNames.Location
import uk.gov.hmrc.RequestUtils.{ escapeURLRegex, extractParam, redirectLocation }

object ValidateBankDetailsFrontEndRequests extends ServicesConfiguration {

  val baseUrl = s"${baseUrlFor("bank-account-reputation-frontend")}/bank-account-reputation-frontend"
  val csrfPattern = """<input type="hidden" name="csrfToken" value="([^"]+)""""
  val xssHeader = headerRegex("X-XSS-Protection", "1; mode=block")
  val strideAuthLogin : String = baseUrlFor("stride-stub")
  val strideAuthResponse : String = baseUrlFor("stride-auth")

  def getStrideLoginRedirect: HttpRequestBuilder = {
    http("get stride login redirect")
      .get(s"$baseUrl/verify")
      .check(status.is(303))
      .check(header(Location).saveAs("strideStubRedirect"))
  }

  def getStrideIdpStubPage: HttpRequestBuilder = {
    http("get stride IDP page")
      .get("${strideStubRedirect}")
      .check(status.is(303))
  }

  val strideSignIn: HttpRequestBuilder =
    http("post /stride-idp-stub/sign-in")
      .post(s"$strideAuthLogin/stride-idp-stub/sign-in")
      .formParam("usersGivenName", "")
      .formParam("usersSurname", "")
      .formParam("pid", "pid")
      .formParam("emailAddress", "")
      .formParam("status", true)
      .formParam("signature", "valid")
      .formParam("roles", "")
      .formParam("RelayState", s"successURL=s$baseUrl/verify")
      .check(status.is(_ ⇒ 303))
      .check(redirectLocation(s"${escapeURLRegex("/stride-idp-stub/redirect-to-stride")}.*").saveAs("confirm-sign-in-redirect"))

  val postAuthResponse: HttpRequestBuilder =
    http("post /stride/auth-response")
      .post(s"$strideAuthResponse/stride/auth-response")
      .formParam("SAMLResponse", s ⇒ URLDecoder.decode(extractParam(s, "confirm-sign-in-redirect")("encodedSamlResponse"), "UTF-8"))
      .formParam("RelayState", s"successURL=/help-to-save-stride/check-eligibility-page&failureURL=/stride/failure?continueURL=/help-to-save-stride/check-eligibility-page")
      .check(status.is(303))
      .check(xssHeader)

  val navigateToBARSFrontendHomePage: HttpRequestBuilder =
    http("Navigate to Home Page")
      .get(s"$baseUrl/verify")
      .check(regex(_ => csrfPattern).saveAs("csrfToken"))
      .check(status.is(200))

  val validateBankDetailsFrontend: HttpRequestBuilder = {
    http("Submit sort code and account number")
      .post(s"$baseUrl/verify")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("input.account.sortCode", "${sortCode}")
      .formParam("input.account.accountNumber", "71201948")
      .formParam("input.subject.name", "Fred Jones")
      .formParam("input.account.accountType", "personal")
      .check(status.is(200))
      .check(substring("${sortCode}"))
      .check(substring("71201948"))
  }
}
