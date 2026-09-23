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
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.{SDECThread, SDECThreadStatus}

import java.time.{LocalDate, LocalDateTime}

given BaseColumnType[SDECThreadStatus] =
  MappedColumnType.base[SDECThreadStatus, String](
    _.toString,
    SDECThreadStatus.valueOf
  )

class SDECThreadTable(tag: Tag) extends Table[SDECThread](tag, "sdec_thread") {

  def id                   = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def reference            = column[String]("reference")
  def status               = column[SDECThreadStatus]("status")
  def createdBy            = column[Long]("created_by")
  def createdTimeStamp     = column[LocalDateTime]("created_timestamp")
  def lastUpdatedTimeStamp = column[LocalDateTime]("last_updated_timestamp")
  def threadExpiryDate     = column[LocalDate]("thread_expiry_date")
  def caseReference        = column[Option[String]]("case_reference")
  def recipientId          = column[Option[Long]]("recipient_id")
  def email                = column[String]("email")
  def nino                 = column[Option[String]]("nino")
  def message              = column[String]("message")
  def requiredBy           = column[Option[LocalDate]]("required_by")

  override def * =
    (
      id,
      reference,
      status,
      createdBy,
      createdTimeStamp,
      lastUpdatedTimeStamp,
      threadExpiryDate,
      caseReference,
      recipientId,
      email,
      nino,
      message,
      requiredBy
    ).mapTo[SDECThread]
}
