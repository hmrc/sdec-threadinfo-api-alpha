/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.sdecthreadinfoapialpha.model.requests

import uk.gov.hmrc.sdecthreadinfoapialpha.model.dto.RecipientDetails

import scala.util.Random

case class ExternalUser(
  internalId:  Option[String],
  firstName:   String,
  lastName:    String,
  email:       String,
  phoneNumber: Option[String],
  nino:        String
)

object ExternalUser {

  def getExternalUserByRecipientDetails(details: RecipientDetails): ExternalUser =
    ExternalUser(
      internalId = Some(Random.alphanumeric.take(10).mkString),
      firstName = details.firstName,
      lastName = details.lastName,
      email = details.email,
      phoneNumber = Option(details.phoneNumber),
      nino = details.nationalInsuranceNumber
    )

  def getRecipientById(threadId: String): ExternalUser =
    if threadId == "THREAD1000AA" then ExternalUser(Some("12345"), "John", "Smith", "user@test.com", None, "WM111111D")
    else
      ExternalUser(
        Some(Random.alphanumeric.take(5).mkString),
        Random.alphanumeric.dropWhile(_.isDigit).take(10).mkString,
        Random.alphanumeric.dropWhile(_.isDigit).take(10).mkString,
        Random.alphanumeric.dropWhile(_.isDigit).take(15).mkString,
        None,
        ""
      )

}
