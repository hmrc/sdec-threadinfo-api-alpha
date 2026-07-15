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

import com.github.blemale.scaffeine.{Cache, Scaffeine}
import uk.gov.hmrc.sdecthreadinfoapialpha.exceptions.ThreadReferenceNotFoundException
import uk.gov.hmrc.sdecthreadinfoapialpha.model.{ThreadReference, ThreadStatus}
import uk.gov.hmrc.sdecthreadinfoapialpha.repository.ThreadReferenceRepositoryAlgebra

import java.time.{LocalDate, LocalDateTime}
import javax.inject.Singleton
import scala.concurrent.Future

@Singleton
class ThreadReferenceRepository extends ThreadReferenceRepositoryAlgebra {

  private val threadReferenceCache: Cache[String, ThreadReference] = Scaffeine()
    .build[String, ThreadReference]()

  seedDummyData()

  private def seedDummyData(): Unit = {
    insertThreadReference(
      ThreadReference(
        id = "123456ABCDEF",
        threadReference = "THREAD-001",
        status = ThreadStatus.Active,
        createdTimeStamp = LocalDateTime.now().minusDays(2),
        lastUpdatedTimeStamp = LocalDateTime.now().minusHours(3),
        threadExpiryDate = LocalDate.now().plusDays(28),
        associatedCaseReference = "CASE-001"
      )
    )

    insertThreadReference(
      ThreadReference(
        id = "DASISTGUT123",
        threadReference = "THREAD-002",
        status = ThreadStatus.Draft,
        createdTimeStamp = LocalDateTime.now().minusDays(1),
        lastUpdatedTimeStamp = LocalDateTime.now().minusHours(2),
        threadExpiryDate = LocalDate.now().plusDays(28),
        associatedCaseReference = "CASE-002"
      )
    )
  }

  def insertThreadReference(threadRef: ThreadReference): Future[Unit] = {
    threadReferenceCache.put(threadRef.id, threadRef)
    Future.successful(())
  }

  override def getByThreadReference(id: String): Future[ThreadReference] =
    threadReferenceCache
      .getIfPresent(id)
      .fold(
        Future.failed(ThreadReferenceNotFoundException(id))
      )(Future.successful)
}
