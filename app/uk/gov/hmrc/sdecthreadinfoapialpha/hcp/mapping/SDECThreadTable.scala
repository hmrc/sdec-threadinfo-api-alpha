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
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.{SDECThread, SDECThreadStatus}

import java.time.LocalDate
import java.time.LocalDateTime

import SDECThreadMappings.given

class SDECThreadTable(tag: Tag) extends Table[SDECThread](tag, "sdec_thread"):

  def id: Rep[Long] =
    column[Long]("id", O.PrimaryKey, O.AutoInc)

  def reference: Rep[String] =
    column[String]("reference")

  def status: Rep[SDECThreadStatus] =
    column[SDECThreadStatus]("status")

  def createdTimeStamp: Rep[LocalDateTime] =
    column[LocalDateTime]("created_timestamp")

  def lastUpdatedTimeStamp: Rep[LocalDateTime] =
    column[LocalDateTime]("last_updated_timestamp")

  def threadExpiryDate: Rep[LocalDate] =
    column[LocalDate]("thread_expiry_date")

  def caseReference: Rep[String] =
    column[String]("case_reference")

  def email: Rep[String] =
    column[String]("email")

  def nino: Rep[String] =
    column[String]("nino")

  override def * : ProvenShape[SDECThread] =
    (
      id,
      reference,
      status,
      createdTimeStamp,
      lastUpdatedTimeStamp,
      threadExpiryDate,
      caseReference,
      email,
      nino
    ).mapTo[SDECThread]
