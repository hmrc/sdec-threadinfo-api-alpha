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

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.hmrc.sdecthreadinfoapialpha.model.dto.ThreadSummary
import uk.gov.hmrc.sdecthreadinfoapialpha.model.query.ThreadSummaryQuery

class ThreadSummaryRepositorySpec extends AnyWordSpec with Matchers {

  private val testRepository: ThreadSummaryRepository = new ThreadSummaryRepository

  private val allThreads: Seq[ThreadSummary] = testRepository.getAll(ThreadSummaryQuery()).futureValue
  private val ownerA:     String             = allThreads.flatMap(_.threadOwner).head

  private def queryFor(threadOwner: String): ThreadSummaryQuery = ThreadSummaryQuery(threadOwner = Some(threadOwner))

  "getAll" must {
    "return all threads for an empty query" in {
      allThreads must not be empty
    }

    "return only the threads owned by the requested owner" in {
      val result = testRepository.getAll(queryFor(ownerA)).futureValue

      result must not be empty
      result.forall(_.threadOwner.contains(ownerA)) mustBe true
    }

    "return nothing when no thread is owned by the requested owner" in {
      testRepository.getAll(queryFor("NOT-AN-OWNER")).futureValue mustBe empty
    }

    "not return threads without a thread owner when a thread owner is requested" in {
      val unownedReferences  = allThreads.filter(_.threadOwner.isEmpty).map(_.threadReference)
      val returnedReferences = testRepository.getAll(queryFor(ownerA)).futureValue.map(_.threadReference)

      unownedReferences  must not be empty
      returnedReferences must contain noElementsOf unownedReferences
    }
  }

}
