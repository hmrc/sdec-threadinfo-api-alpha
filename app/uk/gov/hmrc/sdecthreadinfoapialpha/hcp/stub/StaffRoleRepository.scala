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
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.mapping.StaffRoleTable
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.StaffRoleRepositoryAlgebra
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.StaffRole

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class StaffRoleRepository @Inject() (
  dbConfigProvider: DatabaseConfigProvider
)(using ExecutionContext)
    extends StaffRoleRepositoryAlgebra {
  private val db = dbConfigProvider.get[H2Profile].db

  private val staffRoles = TableQuery[StaffRoleTable]

  def findById(id: Long): Future[Option[StaffRole]] =
    db.run(
      staffRoles
        .filter(_.id === id)
        .result
        .headOption
    )

  def findByStaffId(staffId: Long): Future[Seq[StaffRole]] =
    db.run(
      staffRoles
        .filter(_.staffId === staffId)
        .result
    )

  def findByTeamId(teamId: Long): Future[Seq[StaffRole]] =
    db.run(
      staffRoles
        .filter(_.teamId === teamId)
        .result
    )

  def findByStaffAndTeam(
    staffId: Long,
    teamId:  Long
  ): Future[Option[StaffRole]] =
    db.run(
      staffRoles
        .filter(role =>
          role.staffId === staffId &&
            role.teamId === teamId
        )
        .result
        .headOption
    )

  def insert(staffRole: StaffRole): Future[Long] =
    db.run(
      (staffRoles returning staffRoles.map(_.id)) += staffRole
    )

  def update(staffRole: StaffRole): Future[Int] =
    db.run(
      staffRoles
        .filter(_.id === staffRole.id)
        .update(staffRole)
    )

  def delete(id: Long): Future[Int] =
    db.run(
      staffRoles
        .filter(_.id === id)
        .delete
    )
}
