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

import java.time.LocalDate
import javax.inject.Singleton
import scala.concurrent.Future

@Singleton
class ThreadSummaryRepository {

  private val threads: Seq[ThreadSummary] = Seq(
    ThreadSummary(
      threadReference = "THREAD1000AA",
      relatedReference = Some("QQ 12 34 56 C"),
      externalContact = "Hunter Sage",
      status = "Waiting",
      waitingOn = "External",
      deadline = Some(LocalDate.now().minusDays(2))
    ),
    ThreadSummary(
      threadReference = "THREAD2000BB",
      relatedReference = None,
      externalContact = "Jimmie Worthy",
      status = "Waiting",
      waitingOn = "External",
      deadline = Some(LocalDate.now().minusDays(1))
    ),
    ThreadSummary(
      threadReference = "THREAD3000CC",
      relatedReference = Some("CMS-62-02-43"),
      externalContact = "Jeanette Meador",
      status = "Waiting",
      waitingOn = "External",
      deadline = Some(LocalDate.now().plusDays(14))
    ),
    ThreadSummary(
      threadReference = "THREAD4000DD",
      relatedReference = Some("QQ 12 34 56 C"),
      externalContact = "Ansley Handy",
      status = "Needs action",
      waitingOn = "Internal",
      deadline = None
    ),
    ThreadSummary(
      threadReference = "THREAD5000EE",
      relatedReference = None,
      externalContact = "Sydnee Mansfield",
      status = "In progress",
      waitingOn = "Internal",
      deadline = None
    )
  )

  def getAll: Future[Seq[ThreadSummary]] =
    Future.successful(threads)
}
