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

package uk.gov.hmrc.sdecthreadinfoapialpha.hcp.mapping

import slick.jdbc.H2Profile.api.*
import slick.lifted.ProvenShape
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.SDECRecipient

class SDECRecipientTable(tag: Tag) extends Table[SDECRecipient](tag, "sdec_recipient") {

  def id:          Rep[Long]           = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def internalId:  Rep[Option[String]] = column[Option[String]]("internal_id")
  def firstName:   Rep[String]         = column[String]("first_name")
  def lastName:    Rep[String]         = column[String]("last_name")
  def email:       Rep[String]         = column[String]("email")
  def phoneNumber: Rep[Option[String]] = column[Option[String]]("phone_number")
  def nino:        Rep[String]         = column[String]("nino")

  override def * : ProvenShape[SDECRecipient] =
    (
      id,
      internalId,
      firstName,
      lastName,
      email,
      phoneNumber,
      nino
    ).mapTo[SDECRecipient]
}
