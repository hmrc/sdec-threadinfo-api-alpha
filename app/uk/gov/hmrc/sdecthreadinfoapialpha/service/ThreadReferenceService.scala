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
import uk.gov.hmrc.sdecthreadinfoapialpha.exceptions.InvalidThreadReferenceException
import uk.gov.hmrc.sdecthreadinfoapialpha.model.ThreadReference
import uk.gov.hmrc.sdecthreadinfoapialpha.repository.ThreadReferenceRepositoryAlgebra

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class ThreadReferenceService @Inject() (
    threadReferenceRepository: ThreadReferenceRepositoryAlgebra
) extends ThreadReferenceServiceAlgebra
    with Logging {

  private val threadReferencePattern = "^[A-Z0-9]{12}$".r

  override def getThreadInfoByThreadId(threadId: String): Future[ThreadReference] = {
    logger.info(s"Checking if $threadId exists in the database")
    if (threadReferencePattern.matches(threadId)) {
      threadReferenceRepository.getByThreadReference(threadId)
    } else {
      Future.failed(InvalidThreadReferenceException(threadId))
    }
  }
}
