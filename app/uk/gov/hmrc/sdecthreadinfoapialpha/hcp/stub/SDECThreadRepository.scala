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

import play.api.Logging
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.H2Profile
import slick.jdbc.H2Profile.api.*
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.mapping.SDECThreadTable
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.SDECThreadRepositoryAlgebra
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.SDECThread

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SDECThreadRepository @Inject() (
  dbConfigProvider: DatabaseConfigProvider
)(using ExecutionContext)
    extends SDECThreadRepositoryAlgebra
    with Logging {
  private val db = dbConfigProvider.get[H2Profile].db

  private val sdecThreads = TableQuery[SDECThreadTable]

  def findById(id: Long): Future[Option[SDECThread]] =
    db.run(
      sdecThreads
        .filter(_.id === id)
        .result
        .headOption
    )

  def findByReference(reference: String): Future[Option[SDECThread]] = {
    logger.info(s"Finding $reference")
    db.run(
      sdecThreads
        .filter(_.reference === reference)
        .result
        .headOption
    )
  }

  def findByCreatedBy(staffId: Long): Future[Seq[SDECThread]] =
    db.run(
      sdecThreads
        .filter(_.createdBy === staffId)
        .result
    )

  def findByRecipientId(recipientId: Long): Future[Seq[SDECThread]] =
    db.run(
      sdecThreads
        .filter(_.recipientId === recipientId)
        .result
    )

  def findAll(): Future[Seq[SDECThread]] =
    db.run(
      sdecThreads.result
    )

  def insert(thread: SDECThread): Future[Long] =
    db.run(
      (sdecThreads returning sdecThreads.map(_.id)) += thread
    )

  def update(thread: SDECThread): Future[Int] =
    db.run(
      sdecThreads
        .filter(_.id === thread.id)
        .update(thread)
    )

  def delete(id: Long): Future[Int] =
    db.run(
      sdecThreads
        .filter(_.id === id)
        .delete
    )
}
