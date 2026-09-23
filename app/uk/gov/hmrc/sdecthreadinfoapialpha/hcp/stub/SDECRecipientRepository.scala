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

package uk.gov.hmrc.sdecthreadinfoapialpha.hcp.stub

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.H2Profile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.H2Profile.api.*
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.mapping.SDECRecipientTable
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.SDECRecipientRepositoryAlgebra
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.SDECRecipient

@Singleton
class SDECRecipientRepository @Inject() (
  dbConfigProvider: DatabaseConfigProvider
)(using ExecutionContext)
    extends SDECRecipientRepositoryAlgebra {
  private val db = dbConfigProvider.get[H2Profile].db

  private val sdecRecipients = TableQuery[SDECRecipientTable]

  def findById(id: Long): Future[Option[SDECRecipient]] =
    db.run(
      sdecRecipients
        .filter(_.id === id)
        .result
        .headOption
    )

  def findByInternalId(internalId: String): Future[Option[SDECRecipient]] =
    db.run(
      sdecRecipients
        .filter(_.internalId === internalId)
        .result
        .headOption
    )

  def findAll(): Future[Seq[SDECRecipient]] =
    db.run(
      sdecRecipients.result
    )

  def insert(recipient: SDECRecipient): Future[Long] =
    db.run(
      (sdecRecipients returning sdecRecipients.map(_.id)) += recipient
    )

  def update(recipient: SDECRecipient): Future[Int] =
    db.run(
      sdecRecipients
        .filter(_.id === recipient.id)
        .update(recipient)
    )

  def delete(id: Long): Future[Int] =
    db.run(
      sdecRecipients
        .filter(_.id === id)
        .delete
    )
}
