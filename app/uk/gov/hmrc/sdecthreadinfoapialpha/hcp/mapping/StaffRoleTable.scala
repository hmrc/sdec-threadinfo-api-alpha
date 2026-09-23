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
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.{SRSRole, StaffRole}

given BaseColumnType[SRSRole] =
  MappedColumnType.base[SRSRole, String](
    _.toString,
    SRSRole.valueOf
  )

class StaffRoleTable(tag: Tag) extends Table[StaffRole](tag, "staff_role") {

  def id      = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def staffId = column[Long]("staff_id")
  def teamId  = column[Long]("team_id")
  def srsRole = column[SRSRole]("srs_role")

  def staffFk =
    foreignKey(
      "fk_staff_role",
      staffId,
      TableQuery[SDECStaffTable]
    )(_.id)

  def teamFk =
    foreignKey(
      "fk_team_role",
      teamId,
      TableQuery[SDECTeamTable]
    )(_.id)

  def staffTeamUnique =
    index(
      "uq_staff_team",
      (staffId, teamId),
      unique = true
    )

  override def * =
    (id, staffId, teamId, srsRole).mapTo[StaffRole]
}
