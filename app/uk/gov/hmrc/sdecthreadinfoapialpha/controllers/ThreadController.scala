package uk.gov.hmrc.sdecthreadinfoapialpha.controllers

import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request}
import uk.gov.hmrc.auth.core.{AuthConnector, AuthorisedFunctions}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.sdecthreadinfoapialpha.controllers.actions.IdentifierAction
import uk.gov.hmrc.sdecthreadinfoapialpha.model.requests.IdentifierRequest
import uk.gov.hmrc.sdecthreadinfoapialpha.service.ThreadServiceAlgebra

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class ThreadController @Inject() (
  val authConnector: AuthConnector,
  identify:          IdentifierAction,
  cc:                ControllerComponents,
  threadService:     ThreadServiceAlgebra
)(using ec: ExecutionContext)
    extends BackendController(cc)
    with AuthorisedFunctions
    with Logging {

  def getThreadByUserId(pid: String): Action[AnyContent] = {
    logger.info(s"Getting all threads for $pid")
    identify.async { implicit request =>
      request.request.headers.toMap.foreach { (name, value) =>
        logger.debug(s"Header name: $name: value: ${value.mkString(",")}")
      }
      threadService.getByPID(pid).map(dtos => Ok(Json.toJson(dtos)))
    }
  }
}
