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
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.model.SDECRecipient
import slick.lifted.ForeignKeyQuery
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.model.SDECThread
import slick.lifted.ProvenShape

class SDECRecipientTable(tag: Tag) extends Table[SDECRecipient](tag, "sdec_recipient"):

  def id: Rep[Long] =
    column[Long]("id", O.PrimaryKey, O.AutoInc)

  def sdecThreadId: Rep[Long] =
    column[Long]("sdec_thread_id")

  def firstName: Rep[String] =
    column[String]("first_name")

  def lastName: Rep[String] =
    column[String]("last_name")

  def email: Rep[String] =
    column[String]("email")

  def phoneNumber: Rep[String] =
    column[String]("phone_number")

  def nationalInsuranceNumber: Rep[String] =
    column[String]("national_insurance_number")

  def thread: ForeignKeyQuery[SDECThreadTable, SDECThread] =
    foreignKey(
      "fk_sdec_recipient_thread",
      sdecThreadId,
      TableQuery[SDECThreadTable]
    )(_.id)

  override def * : ProvenShape[SDECRecipient] =
    (
      id,
      sdecThreadId,
      firstName,
      lastName,
      email,
      phoneNumber,
      nationalInsuranceNumber
    ).mapTo[SDECRecipient]
