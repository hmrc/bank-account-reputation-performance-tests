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

package uk.gov.hmrc

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object RequestUtils {
  val csrfPattern = """name="csrfToken" value="([^"]+)""""
  def saveCsrfToken = regex(_ => csrfPattern).saveAs("csrfToken")

  //For Stride Auth
  val RelayStatePattern = """name="RelayState" value="([^"]+)"""
  def saveRelayState = {
    regex(_ => RelayStatePattern).saveAs("strideRelayState")
  }

  val SAMLResponsePattern = """name="SAMLResponse" value="([^"]+)"""
  def saveSAMLResponse = {
    regex(_ => SAMLResponsePattern).saveAs("samlResponse")
  }

  def escapeURLRegex(url: String): String =
    url.replace("/", "\\/")
      .replace(".", "\\.")
      .replace("?", "\\?")

  def redirectLocation(expectedRedirectLocationsRegex: String*) =
    headerRegex("Location", expectedRedirectLocationsRegex.mkString("|"))

  def extractParam(session: Session, key: String)(queryParam: String): String =
    extractParams(session(key).as[String]).getOrElse(queryParam, sys.error("error"))

  def extractParams(url: String): Map[String, String] = {
    val splitUrl = url.split('?').toList
    splitUrl match {
      case _ :: params :: Nil =>
        val paramsList = params.split('&').toList
        paramsList.map(_.split('=').toList match {
          case key :: value :: Nil => key -> value
          case _ => sys.error("")
        }).toMap
      case _ => sys.error("")
    }
  }
}