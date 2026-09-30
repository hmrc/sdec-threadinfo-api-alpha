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

import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.sdecthreadinfoapialpha.exceptions.ThreadSummaryRetrievalException
import uk.gov.hmrc.sdecthreadinfoapialpha.model.query.ThreadSummaryQuery
import uk.gov.hmrc.sdecthreadinfoapialpha.service.ThreadSummaryServiceAlgebra

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ThreadSummaryController @Inject() (
  cc:                   ControllerComponents,
  threadSummaryService: ThreadSummaryServiceAlgebra
)(using ec: ExecutionContext)
    extends BackendController(cc)
    with Logging {

  def getAll(query: ThreadSummaryQuery): Action[AnyContent] = Action.async {
    threadSummaryService
      .getAll(query)
      .map(threads => Ok(Json.toJson(threads)))
      .recover { case exception: ThreadSummaryRetrievalException =>
        logger.error(exception.getMessage, exception)

        InternalServerError(
          Json.obj("message" -> exception.getMessage)
        )
      }
  }

}
