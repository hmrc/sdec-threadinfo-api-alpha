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

package uk.gov.hmrc.sdecthreadinfoapialpha.service

import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.SDECTeamRepositoryAlgebra
import uk.gov.hmrc.sdecthreadinfoapialpha.model.dto.Team
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.SDECTeam

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TeamServiceSpec extends AnyWordSpec with Matchers {

  private val pid: String = "1001"

  private val taskBasedTeamEntity:    SDECTeam = SDECTeam(id = 1L, srsName = "child_benefit", isTaskBased = true)
  private val nonTaskBasedTeamEntity: SDECTeam = SDECTeam(id = 2L, srsName = "vat", isTaskBased = false)

  private val taskBasedTeam:    Team = Team(name = "child_benefit", taskBased = true)
  private val nonTaskBasedTeam: Team = Team(name = "vat", taskBased = false)

  "findByPid" must {
    "return the teams when the staff belongs to teams" in {
      val teamRepository = mock[SDECTeamRepositoryAlgebra]
      when(teamRepository.findByPid(pid))
        .thenReturn(Future.successful(Seq(taskBasedTeamEntity, nonTaskBasedTeamEntity)))

      TeamService(teamRepository).findTeamsByPid(pid).futureValue mustBe Seq(taskBasedTeam, nonTaskBasedTeam)

    }

    "return no teams if the staff belongs to no teams" in {
      val teamRepository = mock[SDECTeamRepositoryAlgebra]
      when(teamRepository.findByPid(pid)).thenReturn(Future.successful(Seq.empty))

      TeamService(teamRepository).findTeamsByPid(pid).futureValue mustBe empty
    }
  }
}
