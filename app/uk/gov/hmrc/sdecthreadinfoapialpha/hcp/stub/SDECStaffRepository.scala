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
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.mapping.SDECStaffTable
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.SDECStaffRepositoryAlgebra
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.SDECStaff

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SDECStaffRepository @Inject() (
  dbConfigProvider: DatabaseConfigProvider
)(using ExecutionContext)
    extends SDECStaffRepositoryAlgebra {
  private val db = dbConfigProvider.get[H2Profile].db

  private val sdecStaff = TableQuery[SDECStaffTable]

  def findById(id: Long): Future[Option[SDECStaff]] =
    db.run(
      sdecStaff
        .filter(_.id === id)
        .result
        .headOption
    )

  def findByPid(pid: String): Future[Option[SDECStaff]] =
    db.run(
      sdecStaff
        .filter(_.pid === pid)
        .result
        .headOption
    )

  def findAll(): Future[Seq[SDECStaff]] =
    db.run(
      sdecStaff.result
    )

  def insert(staff: SDECStaff): Future[Long] =
    db.run(
      (sdecStaff returning sdecStaff.map(_.id))
        += staff
    )

  def update(staff: SDECStaff): Future[Int] =
    db.run(
      sdecStaff
        .filter(_.id === staff.id)
        .update(staff)
    )

  def delete(id: Long): Future[Int] =
    db.run(
      sdecStaff
        .filter(_.id === id)
        .delete
    )
}
