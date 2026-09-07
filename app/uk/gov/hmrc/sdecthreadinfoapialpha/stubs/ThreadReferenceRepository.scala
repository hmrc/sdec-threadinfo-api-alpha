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
import uk.gov.hmrc.sdecthreadinfoapialpha.model.*
import uk.gov.hmrc.sdecthreadinfoapialpha.repository.ThreadReferenceRepositoryAlgebra

import java.time.{LocalDate, LocalDateTime}
import java.util.UUID
import javax.inject.Singleton
import scala.concurrent.Future

@Singleton
class ThreadReferenceRepository extends ThreadReferenceRepositoryAlgebra {

  private val threadReferenceCache: Cache[String, ThreadReference] = Scaffeine()
    .build[String, ThreadReference]()

  seedDummyData()

  private def seedDummyData(): Unit =
    insertThreadReference(
      ThreadReference(
        id = "123456ABCDEF",
        status = ThreadStatus.Active,
        createdTimeStamp = LocalDateTime.now().minusDays(2),
        lastUpdatedTimeStamp = LocalDateTime.now().minusHours(3),
        threadExpiryDate = LocalDate.now().plusDays(28),
        associatedCaseReference = "CASE-001",
        recipientDetails = RecipientDetails(
          firstName = "John",
          lastName = "Smith",
          email = "JohnS@hotmail.com",
          phoneNumber = "07123456789",
          nationalInsuranceNumber = "QQQQQQQQC",
          hasRelatedCase = false,
          caseReferenceNumber = None
        ),
        threadDetails = ThreadDetails(
          message = "Enter default response message",
          responseDate = LocalDate.now().plusDays(7)
        )
      )
    )

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

  override def createThread(request: CreateThreadRequest): Future[ThreadReference] = {

    val generatedThreadReference =
      UUID.randomUUID().toString.replace("-", "").take(12).toUpperCase

    val now = LocalDateTime.now()

    val threadReference =
      ThreadReference(
        id = generatedThreadReference,
        status = ThreadStatus.Active,
        createdTimeStamp = now,
        lastUpdatedTimeStamp = now,
        threadExpiryDate = request.threadDetails.responseDate,
        associatedCaseReference = request.recipientDetails.caseReferenceNumber.getOrElse(""),
        request.recipientDetails,
        request.threadDetails
      )

    threadReferenceCache.put(
      threadReference.id,
      threadReference
    )

    Future.successful(threadReference)
  }
}
