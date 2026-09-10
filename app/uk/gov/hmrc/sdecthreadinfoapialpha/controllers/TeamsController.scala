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
import uk.gov.hmrc.sdecthreadinfoapialpha.stubs.TeamsRepository

import javax.inject.Singleton

@Singleton
class TeamsController @Inject() (
  cc:              ControllerComponents,
  teamsRepository: TeamsRepository
) extends BackendController(cc)
    with Logging {

  def getTeam(teamId: String): Action[AnyContent] = Action {

    teamsRepository.getTeam(teamId) match {
      case Some(team) =>
        Ok(Json.toJson(team))
      case None =>
        logger.warn(s"Team $teamId was not found")
        NotFound(Json.obj("message" -> s"Team $teamId was not found"))
    }
  }

  def getAll: Action[AnyContent] = Action {
    Ok(Json.toJson(teamsRepository.getAll))
  }
}
