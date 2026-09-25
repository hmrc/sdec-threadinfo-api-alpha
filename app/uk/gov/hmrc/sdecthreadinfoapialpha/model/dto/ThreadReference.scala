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

package uk.gov.hmrc.sdecthreadinfoapialpha.model.dto

import play.api.libs.json.{Format, Json}
import uk.gov.hmrc.sdecthreadinfoapialpha.model.Team
import uk.gov.hmrc.sdecthreadinfoapialpha.model.dto.ThreadStatus.Draft
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.{SDECRecipient, SDECThread}

import java.time.{LocalDate, LocalDateTime}

case class ThreadReference(
  id:                      String,
  status:                  ThreadStatus,
  createdTimeStamp:        LocalDateTime,
  lastUpdatedTimeStamp:    LocalDateTime,
  threadExpiryDate:        LocalDate,
  associatedCaseReference: String,
  threadCreator:           String,
  threadOwner:             Option[String],
  owningTeam:              Team,
  recipientDetails:        RecipientDetails,
  threadDetails:           ThreadDetails
)

object ThreadReference {

  implicit val format: Format[ThreadReference] = Json.format[ThreadReference]

  def convertFromEntities(thread: SDECThread, recipient: SDECRecipient): ThreadReference =
    ThreadReference(
      id = thread.reference,
      status = ThreadStatus.fromEntity(thread.status),
      createdTimeStamp = thread.createdTimeStamp,
      lastUpdatedTimeStamp = thread.lastUpdatedTimeStamp,
      threadExpiryDate = thread.threadExpiryDate,
      associatedCaseReference = thread.caseReference.getOrElse("No case reference"),
      threadCreator = thread.threadCreator,
      threadOwner = thread.threadOwner,
      owningTeam = Team(thread.owningTeamName, thread.owningTeamType),
      recipientDetails = RecipientDetails(
        firstName = recipient.firstName,
        lastName = recipient.lastName,
        email = recipient.email,
        phoneNumber = recipient.phoneNumber.getOrElse("No phone number"),
        nationalInsuranceNumber = recipient.nino,
        hasRelatedCase = thread.caseReference.isDefined,
        caseReferenceNumber = thread.caseReference
      ),
      threadDetails = ThreadDetails(
        message = thread.message,
        responseDate = thread.requiredBy.getOrElse(LocalDate.now().plusYears(1L))
      )
    )

  def convertFromThreadEntity(thread: SDECThread): ThreadReference =
    ThreadReference(
      id = thread.reference,
      status = ThreadStatus.fromEntity(thread.status),
      createdTimeStamp = thread.createdTimeStamp,
      lastUpdatedTimeStamp = thread.lastUpdatedTimeStamp,
      threadExpiryDate = thread.threadExpiryDate,
      associatedCaseReference = thread.caseReference.getOrElse("No case reference"),
      threadCreator = thread.threadCreator,
      threadOwner = thread.threadOwner,
      owningTeam = Team(thread.owningTeamName, thread.owningTeamType),
      recipientDetails = getEmptyRecipient,
      threadDetails = ThreadDetails(
        message = thread.message,
        responseDate = thread.requiredBy.getOrElse(LocalDate.now().plusYears(1L))
      )
    )

  def getEmptyThread: ThreadReference =
    ThreadReference(
      id = "",
      status = Draft,
      createdTimeStamp = LocalDateTime.now(),
      lastUpdatedTimeStamp = LocalDateTime.now(),
      threadExpiryDate = LocalDate.now(),
      associatedCaseReference = "",
      threadCreator = "",
      threadOwner = None,
      owningTeam = Team(name = "", taskBased = false),
      recipientDetails = getEmptyRecipient,
      threadDetails = ThreadDetails(message = "", responseDate = LocalDate.now())
    )

  private def getEmptyRecipient: RecipientDetails =
    RecipientDetails(
      firstName = "",
      lastName = "",
      email = "",
      phoneNumber = "",
      nationalInsuranceNumber = "",
      hasRelatedCase = false,
      caseReferenceNumber = None
    )

}
