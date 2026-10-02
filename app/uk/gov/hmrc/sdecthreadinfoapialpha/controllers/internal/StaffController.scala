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

package uk.gov.hmrc.sdecthreadinfoapialpha.controllers.internal

import play.api.Logging
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.sdecthreadinfoapialpha.model.dto.{StaffAccessRequest, StaffAccessResponse}
import uk.gov.hmrc.sdecthreadinfoapialpha.service.StaffServiceAlgebra

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class StaffController @Inject() (
  cc:           ControllerComponents,
  staffService: StaffServiceAlgebra
)(using ec: ExecutionContext)
    extends BackendController(cc)
    with Logging {

  def validateAccess(): Action[AnyContent] =
    Action.async { implicit request =>
      request.body.asJson match {

        case Some(json) =>
          json.validate[StaffAccessRequest] match {

            case JsSuccess(accessRequest, _) =>

              staffService
                .validateAccess(
                  accessRequest.pid,
                  accessRequest.role
                )
                .map { authorised =>
                  Ok(
                    Json.toJson(
                      StaffAccessResponse(authorised)
                    )
                  )
                }

            case JsError(errors) =>
              scala.concurrent.Future.successful(
                BadRequest(Json.obj("message" -> "Invalid request"))
              )
          }

        case None =>
          scala.concurrent.Future.successful(
            BadRequest(Json.obj("message" -> "Missing JSON body"))
          )
      }
    }
}
