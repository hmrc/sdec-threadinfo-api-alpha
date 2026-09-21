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
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.model.SDECRecipient
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.SDECRecipientRepositoryAlgebra

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SDECRecipientRepository @Inject() (
  dbConfigProvider: DatabaseConfigProvider
)(using ExecutionContext)
    extends SDECRecipientRepositoryAlgebra:

  private val db = dbConfigProvider.get[H2Profile].db

  override def insert(recipient: SDECRecipient): Future[SDECRecipient] =
    val insertQuery =
      SDECTables.recipients returning SDECTables.recipients.map(_.id) into { case (recipient, id) =>
        recipient.copy(id = id)
      }

    db.run(insertQuery += recipient)

  override def update(recipient: SDECRecipient): Future[SDECRecipient] =
    db.run(
      SDECTables.recipients
        .filter(_.id === recipient.id)
        .update(recipient)
    ).map{rowsUpdated =>
      if rowsUpdated == 1 then
        recipient
      else
        throw new NoSuchElementException(s"SDECThread with id ${recipient.id} was not found")
    }

  override def getById(id: Long): Future[Option[SDECRecipient]] =
    db.run(
      SDECTables.recipients
        .filter(_.id === id)
        .result
        .headOption
    )
