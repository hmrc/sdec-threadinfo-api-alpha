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
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.mapping.SDECTeamTable
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.SDECTeamRepositoryAlgebra
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.SDECTeam

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SDECTeamRepository @Inject() (
  dbConfigProvider: DatabaseConfigProvider
)(using ExecutionContext)
    extends SDECTeamRepositoryAlgebra {
  private val db = dbConfigProvider.get[H2Profile].db

  private val sdecTeams = TableQuery[SDECTeamTable]

  def findById(id: Long): Future[Option[SDECTeam]] =
    db.run(
      sdecTeams
        .filter(_.id === id)
        .result
        .headOption
    )

  def findBySrsName(srsName: String): Future[Option[SDECTeam]] =
    db.run(
      sdecTeams
        .filter(_.srsName === srsName)
        .result
        .headOption
    )

  def findAll(): Future[Seq[SDECTeam]] =
    db.run(
      sdecTeams.result
    )

  def insert(team: SDECTeam): Future[Long] =
    db.run(
      (sdecTeams returning sdecTeams.map(_.id)) += team
    )

  def update(team: SDECTeam): Future[Int] =
    db.run(
      sdecTeams
        .filter(_.id === team.id)
        .update(team)
    )

  def delete(id: Long): Future[Int] =
    db.run(
      sdecTeams
        .filter(_.id === id)
        .delete
    )
}
