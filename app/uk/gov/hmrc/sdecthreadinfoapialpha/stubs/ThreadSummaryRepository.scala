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

import uk.gov.hmrc.sdecthreadinfoapialpha.model.dto.ThreadSummary
import uk.gov.hmrc.sdecthreadinfoapialpha.model.query.ThreadSummaryQuery

import java.time.LocalDate
import javax.inject.Singleton
import scala.concurrent.Future

@Singleton
class ThreadSummaryRepository {

  private val threadSummaries: Seq[ThreadSummary] = Seq(
    ThreadSummary(
      threadReference = "THREAD1000AA",
      relatedReference = Some("QQ 12 34 56 C"),
      externalContact = "Hunter Sage",
      status = "Waiting",
      waitingOn = "External",
      deadline = Some(LocalDate.now().minusDays(2)),
      threadOwner = Some("pid-cb-001")
    ),
    ThreadSummary(
      threadReference = "THREAD2000BB",
      relatedReference = None,
      externalContact = "Jimmie Worthy",
      status = "Waiting",
      waitingOn = "External",
      deadline = Some(LocalDate.now().minusDays(1)),
      threadOwner = Some("pid-cb-001")
    ),
    ThreadSummary(
      threadReference = "THREAD3000CC",
      relatedReference = Some("CMS-62-02-43"),
      externalContact = "Jeanette Meador",
      status = "Waiting",
      waitingOn = "External",
      deadline = Some(LocalDate.now().plusDays(14)),
      threadOwner = None
    ),
    ThreadSummary(
      threadReference = "THREAD4000DD",
      relatedReference = Some("QQ 12 34 56 C"),
      externalContact = "Ansley Handy",
      status = "Needs action",
      waitingOn = "Internal",
      deadline = None,
      threadOwner = Some("pid-pen-001")
    ),
    ThreadSummary(
      threadReference = "THREAD5000EE",
      relatedReference = None,
      externalContact = "Sydnee Mansfield",
      status = "In progress",
      waitingOn = "Internal",
      deadline = None,
      threadOwner = Some("pid-both-001")
    ),
    ThreadSummary(
      threadReference = "THREAD6000FF",
      relatedReference = None,
      externalContact = "Justin Case",
      status = "In progress",
      waitingOn = "Internal",
      deadline = None,
      threadOwner = None
    ),
    ThreadSummary(
      threadReference = "THREAD7000GG",
      relatedReference = None,
      externalContact = "Jane Doe",
      status = "Waiting",
      waitingOn = "Internal",
      deadline = None,
      threadOwner = Some("pid-pen-001")
    )
  )

  def getAll(query: ThreadSummaryQuery): Future[Seq[ThreadSummary]] =
    Future.successful(threadSummaries.filter(threadSummary => threadSummary.matches(query)))

  extension (threadSummary: ThreadSummary) {
    private def matches(query: ThreadSummaryQuery): Boolean =
      threadSummary.matchesThreadOwner(query)

    private def matchesThreadOwner(query: ThreadSummaryQuery): Boolean =
      query.threadOwner.forall(requestedOwner => threadSummary.threadOwner.contains(requestedOwner))
  }

}
