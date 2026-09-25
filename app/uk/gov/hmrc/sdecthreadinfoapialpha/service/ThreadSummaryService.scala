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

import play.api.Logging
import uk.gov.hmrc.sdecthreadinfoapialpha.exceptions.ThreadSummaryRetrievalException
import uk.gov.hmrc.sdecthreadinfoapialpha.model.dto.ThreadSummary
import uk.gov.hmrc.sdecthreadinfoapialpha.stubs.ThreadSummaryRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

@Singleton
class ThreadSummaryService @Inject() (
  threadSummaryRepository: ThreadSummaryRepository
)(using ExecutionContext)
    extends ThreadSummaryServiceAlgebra
    with Logging {

  override def getAll: Future[Seq[ThreadSummary]] =
    threadSummaryRepository.getAll
      .recoverWith { case NonFatal(exception) =>
        logger.error("Failed to retrieve thread summaries", exception)
        Future.failed(ThreadSummaryRetrievalException(exception))
      }
}
