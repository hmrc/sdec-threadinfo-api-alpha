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

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.hmrc.sdecthreadinfoapialpha.exceptions.InvalidThreadReferenceException
import uk.gov.hmrc.sdecthreadinfoapialpha.model.{ThreadReference, ThreadStatus}
import uk.gov.hmrc.sdecthreadinfoapialpha.repository.ThreadReferenceRepositoryAlgebra

import java.time.{LocalDate, LocalDateTime}
import scala.concurrent.Future

class ThreadReferenceServiceSpec extends AnyWordSpec with Matchers {

  private val threadReference = ThreadReference(
    id = "1",
    threadReference = "THREAD-001",
    status = ThreadStatus.Active,
    createdTimeStamp = LocalDateTime.parse("2026-06-30T11:05:23"),
    lastUpdatedTimeStamp = LocalDateTime.parse("2026-07-02T08:05:23"),
    threadExpiryDate = LocalDate.parse("2026-07-30"),
    associatedCaseReference = "CASE-001"
  )

  private val repository = new ThreadReferenceRepositoryAlgebra {

    override def insertThreadReference(
        threadRef: ThreadReference
    ): Future[Unit] =
      Future.successful(())

    override def getByThreadReference(
        id: String
    ): Future[ThreadReference] =
      Future.successful(threadReference)
  }

  private val service = new ThreadReferenceService(repository)

  "getThreadInfoByThreadId" should {
    "return the thread reference from the repository" in {
      service
        .getThreadInfoByThreadId("ABCD1234EFGH")
        .futureValue shouldBe threadReference
    }

    "fail for an invalid thread reference" in {
      service
        .getThreadInfoByThreadId("INVALID")
        .failed
        .futureValue shouldBe a[InvalidThreadReferenceException]
    }
  }
}
