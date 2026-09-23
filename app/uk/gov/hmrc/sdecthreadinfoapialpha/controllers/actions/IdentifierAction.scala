package uk.gov.hmrc.sdecthreadinfoapialpha.controllers.actions

import com.google.inject.Inject
import play.api.Logging
import play.api.mvc.Results.Unauthorized
import play.api.mvc.{ActionBuilder, ActionFunction, AnyContent, BodyParsers, Request, Result}
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.{AuthConnector, AuthorisationException, AuthorisedFunctions, NoActiveSession}
import uk.gov.hmrc.http.{HeaderCarrier, UnauthorizedException}
import uk.gov.hmrc.play.http.HeaderCarrierConverter
import uk.gov.hmrc.sdecthreadinfoapialpha.model.requests.IdentifierRequest

import scala.concurrent.{ExecutionContext, Future}

trait IdentifierAction
    extends ActionBuilder[IdentifierRequest, AnyContent]
    with ActionFunction[Request, IdentifierRequest]

class AuthenticatedIdentifierAction @Inject() (
  override val authConnector: AuthConnector,
  val parser:                 BodyParsers.Default
)(implicit val executionContext: ExecutionContext)
    extends IdentifierAction
    with AuthorisedFunctions
    with Logging {

  override def invokeBlock[A](
    request: Request[A],
    block:   IdentifierRequest[A] => Future[Result]
  ): Future[Result] = {

    implicit val hc: HeaderCarrier =
      HeaderCarrierConverter.fromRequest(request)

    authorised().retrieve(Retrievals.internalId) {
      _.map { internalId =>
        block(IdentifierRequest(request, internalId))
      }.getOrElse(throw new UnauthorizedException("Unable to retrieve internal Id"))
    } recover {
      case e: NoActiveSession =>
        logger.warn(s"No Active Session: ${e.reason}")
        Unauthorized
      case e: AuthorisationException =>
        logger.warn(s"Not authorised: ${e.reason}")
        Unauthorized
    }
  }
}
