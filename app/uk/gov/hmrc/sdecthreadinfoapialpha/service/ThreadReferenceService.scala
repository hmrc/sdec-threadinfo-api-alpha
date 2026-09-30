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
import uk.gov.hmrc.sdecthreadinfoapialpha.exceptions.{InvalidThreadReferenceException, StaffNotFoundException}
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.{SDECRecipientRepositoryAlgebra, SDECStaffRepositoryAlgebra, SDECThreadRepositoryAlgebra}
import uk.gov.hmrc.sdecthreadinfoapialpha.model.Team
import uk.gov.hmrc.sdecthreadinfoapialpha.model.dto.{CreateThreadRequest, RecipientDetails, ThreadDetails, ThreadReference}
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.SDECThreadStatus.Active
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.{SDECRecipient, SDECStaff, SDECThread}
import uk.gov.hmrc.sdecthreadinfoapialpha.model.requests.ExternalUser

import java.time.{LocalDate, LocalDateTime}
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

@Singleton
class ThreadReferenceService @Inject() (
  threadRepository:    SDECThreadRepositoryAlgebra,
  recipientRepository: SDECRecipientRepositoryAlgebra,
  staffRepository:     SDECStaffRepositoryAlgebra
)(using ec: ExecutionContext)
    extends ThreadReferenceServiceAlgebra
    with Logging {

  private val threadReferencePattern = "^[A-Z0-9]{12}$".r

  override def getThreadInfoByThreadId(threadId: String, externalUser: ExternalUser): Future[ThreadReference] = {
    logger.info(s"Checking if $threadId exists in the database")
    if threadReferencePattern.matches(threadId) then {
      for {
        threadOption <- threadRepository.findByReference(threadId)
        recipientId = getRecipientIdFromThread(threadOption)
        recipientOption <- getOrStoreRecipient(recipientId, externalUser)
        dto             <- convertEntityToDTO(threadOption, recipientOption)
      } yield dto
    } else {
      Future.failed(InvalidThreadReferenceException(threadId))
    }
  }

  override def createThread(request: CreateThreadRequest, externalUser: ExternalUser): Future[ThreadReference] =
    for {
      creator        <- findOrCreateStaff(request.creatorPid, request.creatorName)
      savedRecipient <- getOrStoreRecipient(None, externalUser)
      thread     = getThreadFromRequest(creator.id, request.owningTeam, request.recipientDetails, request.threadDetails)
      sdecthread = thread.copy(recipientId = savedRecipient.map(_.id))
      threadId    <- threadRepository.insert(sdecthread)
      savedThread <- threadRepository.findById(threadId)
      dto         <- convertEntityToDTO(savedThread, savedRecipient)
    } yield dto

  private def getThreadFromRequest(
    creatorId: Long,
    team:      Team,
    recipient: RecipientDetails,
    thread:    ThreadDetails
  ): SDECThread =
    SDECThread(
      id = 0L,
      reference = Random.alphanumeric.take(12).mkString.toUpperCase,
      status = Active,
      createdBy = creatorId,
      createdTimeStamp = LocalDateTime.now(),
      lastUpdatedTimeStamp = LocalDateTime.now(),
      threadExpiryDate = LocalDate.now.plusMonths(3L),
      caseReference = recipient.caseReferenceNumber,
      recipientId = None,
      email = recipient.email,
      nino = Some(recipient.nationalInsuranceNumber),
      message = thread.message,
      requiredBy = Some(thread.responseDate),
      threadOwnerId = Option.when(team.taskBased)(creatorId),
      owningTeamName = team.name,
      owningTeamType = team.taskBased
    )

  private def findOrCreateStaff(pid: String, name: Option[String]): Future[SDECStaff] =
    staffRepository.findByPid(pid).flatMap {
      case Some(staff) => Future.successful(staff)
      case None        =>
        val newStaff = SDECStaff(0L, pid, name.filter(_.trim.nonEmpty).getOrElse("Unknown"))
        staffRepository.insert(newStaff).map(id => newStaff.copy(id = id))
    }

  private def findStaffById(id: Long): Future[SDECStaff] =
    staffRepository.findById(id).flatMap {
      case Some(staff) => Future.successful(staff)
      case None        => Future.failed(StaffNotFoundException(id.toString))
    }

  private def findOwner(ownerId: Option[Long]): Future[Option[SDECStaff]] =
    ownerId match {
      case Some(id) => findStaffById(id).map(Some(_))
      case None     => Future.successful(None)
    }

  private def getRecipientIdFromThread(maybeThread: Option[SDECThread]): Option[Long] =
    maybeThread match {
      case Some(value) => value.recipientId
      case None        => None
    }

  private def getOrStoreRecipient(
    recipientId:  Option[Long],
    externalUser: ExternalUser
  ): Future[Option[SDECRecipient]] =
    recipientId match {
      case Some(id) => recipientRepository.findById(id)
      case None     =>
        val recipient = SDECRecipient.convert(externalUser)
        for {
          id    <- recipientRepository.insert(recipient)
          saved <- recipientRepository.findById(id)
        } yield saved
    }

  private def convertEntityToDTO(
    maybeThread:    Option[SDECThread],
    maybeRecipient: Option[SDECRecipient]
  ): Future[ThreadReference] =
    maybeThread match {
      case None         => Future.successful(ThreadReference.getEmptyThread)
      case Some(thread) =>
        for {
          creator <- findStaffById(thread.createdBy)
          owner   <- findOwner(thread.threadOwnerId)
        } yield ThreadReference.convertFromEntities(thread, maybeRecipient, creator, owner)
    }
}
