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

package uk.gov.hmrc.sdecthreadinfoapialpha.stubs

import uk.gov.hmrc.sdecthreadinfoapialpha.model.Team

import javax.inject.Singleton

@Singleton
class TeamsRepository {

  private val teams = Seq(
    Team("TEAM-001", "Child Benefits", taskBased = false, strideRole = "sdec_child_benefits"),
    Team("TEAM-002", "Pensions", taskBased = true, strideRole = "sdec_pensions")
  )

  def getTeam(id: String): Option[Team] = teams.find(_.id == id)

  def getTeamByRole(role: String): Option[Team] =
    teams.find(_.strideRole.equalsIgnoreCase(role))

  def getAll: Seq[Team] = teams
}
