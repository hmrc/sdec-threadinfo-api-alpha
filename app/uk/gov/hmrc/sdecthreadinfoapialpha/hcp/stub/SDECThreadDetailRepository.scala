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
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.SDECThreadDetailRepositoryAlgebra
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.SDECThreadDetail

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SDECThreadDetailRepository @Inject() (
  dbConfigProvider: DatabaseConfigProvider
)(using ExecutionContext)
    extends SDECThreadDetailRepositoryAlgebra:

  private val db = dbConfigProvider.get[H2Profile].db

  override def insert(detail: SDECThreadDetail): Future[SDECThreadDetail] =
    val insertQuery =
      SDECTables.threadDetails returning SDECTables.threadDetails.map(_.id) into { case (detail, id) =>
        detail.copy(id = id)
      }

    db.run(insertQuery += detail)

  override def update(detail: SDECThreadDetail): Future[SDECThreadDetail] =
    db.run(
      SDECTables.threadDetails
        .filter(_.id === detail.id)
        .update(detail)
    ).map { rowsUpdated =>
      if rowsUpdated == 1 then detail
      else throw new NoSuchElementException(s"SDECThread with id ${detail.id} was not found")
    }

  override def getById(id: Long): Future[Option[SDECThreadDetail]] =
    db.run(
      SDECTables.threadDetails
        .filter(_.id === id)
        .result
        .headOption
    )
