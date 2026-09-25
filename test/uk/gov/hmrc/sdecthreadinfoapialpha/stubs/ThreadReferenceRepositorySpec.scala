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

import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.hmrc.sdecthreadinfoapialpha.model.*
import uk.gov.hmrc.sdecthreadinfoapialpha.model.dto.{CreateThreadRequest, RecipientDetails, ThreadDetails, ThreadReference}

import java.time.LocalDate

class ThreadReferenceRepositorySpec extends AnyWordSpec with Matchers {

  private def request(team: Team) = CreateThreadRequest(
    threadCreator = "PID123",
    threadOwner = None,
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

  private def createAndFetch(team: Team): ThreadReference = {
    val repository = new ThreadReferenceRepository
    val created    = repository.createThread(request(team)).futureValue
    repository.getByThreadReference(created.id).futureValue
  }

  "createThread" should {
    "store creator, owner and owning team for a task based team" in {
      val team   = Team("Team A", taskBased = true)
      val stored = createAndFetch(team)

      stored.threadCreator shouldBe "PID123"
      stored.threadOwner   shouldBe Some("PID123")
      stored.owningTeam    shouldBe team
    }

    "store creator and owning team with no owner for a non task based team" in {
      val team   = Team("Team B", taskBased = false)
      val stored = createAndFetch(team)

      stored.threadCreator shouldBe "PID123"
      stored.threadOwner   shouldBe None
      stored.owningTeam    shouldBe team
    }
  }
}
