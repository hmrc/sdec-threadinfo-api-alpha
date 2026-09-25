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

import play.api.libs.json.Json
import play.api.mvc.*
import uk.gov.hmrc.sdecthreadinfoapialpha.model.dto.{CreateThreadRequest, CreateThreadResponse}
import uk.gov.hmrc.sdecthreadinfoapialpha.model.requests.ExternalUser
import uk.gov.hmrc.sdecthreadinfoapialpha.service.ThreadReferenceServiceAlgebra

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class ThreadCreateController @Inject() (
  cc:                     ControllerComponents,
  threadReferenceService: ThreadReferenceServiceAlgebra
)(using ec: ExecutionContext)
    extends AbstractController(cc) {

  def createThread(): Action[CreateThreadRequest] =
    Action.async(parse.json[CreateThreadRequest]) { request =>
      threadReferenceService
        .createThread(request.body, ExternalUser.getExternalUserByRecipientDetails(request.body.recipientDetails))
        .map { thread =>
          Created(
            Json.toJson(
              CreateThreadResponse(
                threadReference = thread.id,
                createdTimeStamp = thread.createdTimeStamp
              )
            )
          )
        }
    }
}
