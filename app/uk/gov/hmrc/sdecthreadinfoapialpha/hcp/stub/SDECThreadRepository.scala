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
import slick.jdbc.H2Profile.api.*
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.model.SDECThread
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.SDECThreadRepositoryAlgebra

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SDECThreadRepository @Inject() (
  dbConfigProvider: DatabaseConfigProvider
)(using ExecutionContext)
    extends SDECThreadRepositoryAlgebra:

  private val db = dbConfigProvider.get[H2Profile].db

  override def insert(thread: SDECThread): Future[SDECThread] =
    val insertQuery =
      SDECTables.threads returning SDECTables.threads.map(_.id) into { case (thread, id) =>
        thread.copy(id = id)
      }

    db.run(insertQuery += thread)

  override def update(thread: SDECThread): Future[SDECThread] =
    db.run(
      SDECTables.threads
        .filter(_.id === thread.id)
        .update(thread)
    ).map {rowsUpdated =>
      if rowsUpdated == 1 then
        thread
      else
        throw new NoSuchElementException(s"SDECThread with id ${thread.id} was not found")
    }

  override def getById(id: Long): Future[Option[SDECThread]] =
    db.run(
      SDECTables.threads
        .filter(_.id === id)
        .result
        .headOption
    )

  override def getByReference(reference: String): Future[Option[SDECThread]] =
    db.run(
      SDECTables.threads
        .filter(_.reference === reference)
        .result
        .headOption
    )
