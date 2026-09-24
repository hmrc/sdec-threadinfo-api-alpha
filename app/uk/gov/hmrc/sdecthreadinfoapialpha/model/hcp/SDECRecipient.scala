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

package uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp

import play.api.libs.json.{Json, OFormat}
import uk.gov.hmrc.sdecthreadinfoapialpha.model.requests.ExternalUser

case class SDECRecipient(
  id:          Long,
  internalId:  Option[String],
  firstName:   String,
  lastName:    String,
  email:       String,
  phoneNumber: Option[String],
  nino:        String
)

object SDECRecipient {
  given OFormat[SDECRecipient] = Json.format[SDECRecipient]

  def convert(externalUser: ExternalUser): SDECRecipient =
    SDECRecipient(
      id = 0L,
      internalId = externalUser.internalId,
      firstName = externalUser.firstName,
      lastName = externalUser.lastName,
      email = externalUser.email,
      phoneNumber = externalUser.phoneNumber,
      nino = externalUser.nino
    )
}
