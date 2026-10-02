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

import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{verify, when}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.*
import uk.gov.hmrc.sdecthreadinfoapialpha.model.Team
import uk.gov.hmrc.sdecthreadinfoapialpha.model.dto.{CreateThreadRequest, RecipientDetails, ThreadDetails}
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.{SDECRecipient, SDECStaff, SDECThread}
import uk.gov.hmrc.sdecthreadinfoapialpha.model.requests.ExternalUser

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ThreadReferenceServiceSpec extends AnyWordSpec with Matchers with ScalaFutures with MockitoSugar {

  private val creator = SDECStaff(7L, "PID123", "John Test")

  private val externalUser =
    ExternalUser(Some("1"), "John", "Smith", "some@example.com", None, "QQ123456C")

  private def request(team: Team) = CreateThreadRequest(
    creatorPid = "PID123",
    creatorName = Some("John Test"),
    owningTeam = team,
    recipientDetails = RecipientDetails("John", "Smith", "some@example.com", "07123456789", "QQ123456C", false, None),
    threadDetails = ThreadDetails("Hello", LocalDate.now().plusDays(7))
  )

  private def insertedThreadFor(team: Team): SDECThread = {
    val threads    = mock[SDECThreadRepositoryAlgebra]
    val recipients = mock[SDECRecipientRepositoryAlgebra]
    val staff      = mock[SDECStaffRepositoryAlgebra]

    when(staff.findByPid("PID123")).thenReturn(Future.successful(Some(creator)))
    when(recipients.insert(any[SDECRecipient])).thenReturn(Future.successful(1L))
    when(recipients.findById(1L)).thenReturn(Future.successful(None))
    when(threads.insert(any[SDECThread])).thenReturn(Future.successful(1L))
    when(threads.findById(1L)).thenReturn(Future.successful(None))

    new ThreadReferenceService(threads, recipients, staff)
      .createThread(request(team), externalUser)
      .futureValue

    val captor = ArgumentCaptor.forClass(classOf[SDECThread])
    verify(threads).insert(captor.capture())
    captor.getValue
  }

  "createThread" should {
    "use the staff id looked up from the PID as createdBy" in {
      insertedThreadFor(Team("Team B", taskBased = false)).createdBy shouldBe 7L
    }

    "set the creator as owner for a task based team" in {
      insertedThreadFor(Team("Team A", taskBased = true)).threadOwnerId shouldBe Some(7L)
    }

    "leave the owner empty for a non task based team" in {
      insertedThreadFor(Team("Team B", taskBased = false)).threadOwnerId shouldBe None
    }
  }
}
