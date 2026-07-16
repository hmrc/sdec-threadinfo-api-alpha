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

package uk.gov.hmrc.sdecthreadinfoapialpha.controllers

import jakarta.inject.Inject
import play.api.Logging
import play.api.libs.json.*
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.sdecthreadinfoapialpha.exceptions.{
  InvalidThreadReferenceException,
  ThreadReferenceNotFoundException
}
import uk.gov.hmrc.sdecthreadinfoapialpha.model.ThreadReference
import uk.gov.hmrc.sdecthreadinfoapialpha.service.ThreadReferenceServiceAlgebra

import javax.inject.Singleton
import scala.concurrent.ExecutionContext

@Singleton
class ThreadReferenceController @Inject() (
    cc: ControllerComponents,
    threadReferenceService: ThreadReferenceServiceAlgebra
)(implicit ec: ExecutionContext)
    extends BackendController(cc)
    with Logging {

  def getThreadReference(threadId: String): Action[AnyContent] = {
    logger.info(s"getThreadReference: Getting ThreadInformation for $threadId")
    Action.async { implicit request =>
      threadReferenceService
        .getThreadInfoByThreadId(threadId)
        .map(tr => Ok(Json.toJson(tr)))
        .recover {
          case e: InvalidThreadReferenceException =>
            logger.warn(s"Thread Reference ID $threadId was invalid")
            BadRequest(Json.obj("message" -> e.getMessage))

          case e: ThreadReferenceNotFoundException =>
            logger.warn(s"Thread Reference ID $threadId was not found")
            NotFound(Json.obj("message" -> e.getMessage))
        }
    }
  }
}
