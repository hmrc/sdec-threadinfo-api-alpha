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

import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.SDECStaffRepositoryAlgebra
import uk.gov.hmrc.sdecthreadinfoapialpha.model.dto.*
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.SDECStaff

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ThreadReferenceRepositorySpec extends AnyWordSpec with Matchers with MockitoSugar {

  private def request(team: Team) = CreateThreadRequest(
    creatorPid = "PID123",
    creatorName = Some("John Test"),
    owningTeam = team,
    recipientDetails = RecipientDetails(
      firstName = "John",
      lastName = "Smith",
      email = "some@example.com",
      phoneNumber = "07123456789",
      nationalInsuranceNumber = "QQ123456C",
      hasRelatedCase = false,
      caseReferenceNumber = None
    ),
    threadDetails = ThreadDetails(
      message = "Hello",
      responseDate = LocalDate.now().plusDays(7)
    )
  )

  private val creator = StaffDetails(7L, "PID123", "John Test")

  private def createAndFetch(team: Team): ThreadReference = {
    val staff = mock[SDECStaffRepositoryAlgebra]
    when(staff.findByPid("PID123"))
      .thenReturn(Future.successful(Some(SDECStaff(7L, "PID123", "John Test"))))

    val repository = new ThreadReferenceRepository(staff)
    val created    = repository.createThread(request(team)).futureValue
    repository.getByThreadReference(created.id).futureValue
  }

  "createThread" should {
    "store creator, owner and owning team for a task based team" in {
      val team   = Team("Team A", taskBased = true)
      val stored = createAndFetch(team)

      stored.createdBy   shouldBe creator
      stored.threadOwner shouldBe Some(creator)
      stored.owningTeam  shouldBe team
    }

    "store creator and owning team with no owner for a non task based team" in {
      val team   = Team("Team B", taskBased = false)
      val stored = createAndFetch(team)

      stored.createdBy   shouldBe creator
      stored.threadOwner shouldBe None
      stored.owningTeam  shouldBe team
    }
  }
}
