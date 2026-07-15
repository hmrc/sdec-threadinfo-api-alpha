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

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status
import play.api.test.Helpers.*
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.sdecthreadinfoapialpha.exceptions.{
  InvalidThreadReferenceException,
  ThreadReferenceNotFoundException
}
import uk.gov.hmrc.sdecthreadinfoapialpha.model.{ThreadReference, ThreadStatus}
import uk.gov.hmrc.sdecthreadinfoapialpha.service.ThreadReferenceServiceAlgebra

import java.time.{LocalDate, LocalDateTime}
import scala.concurrent.{ExecutionContext, Future}

class ThreadReferenceControllerSpec extends AnyWordSpec with Matchers {

  private val threadReference = ThreadReference(
    id = "123456ABCDEF",
    threadReference = "THREAD-001",
    status = ThreadStatus.Active,
    createdTimeStamp = LocalDateTime.parse("2026-06-30T11:05:23"),
    lastUpdatedTimeStamp = LocalDateTime.parse("2026-07-02T08:05:23"),
    threadExpiryDate = LocalDate.parse("2026-07-30"),
    associatedCaseReference = "CASE-001"
  )

  private val threadReferenceService = new ThreadReferenceServiceAlgebra {
    override def getThreadInfoByThreadId(threadId: String): Future[ThreadReference] =
      threadId match {
        case "123456ABCDEF" => Future.successful(threadReference)
        case "ZZZZZZZZZZZZ" =>
          Future.failed(ThreadReferenceNotFoundException(threadId))
        case "999" => Future.failed(InvalidThreadReferenceException("999"))
      }
  }

  private val controller =
    new ThreadReferenceController(
      Helpers.stubControllerComponents(),
      threadReferenceService
    )(ExecutionContext.global)

  "GET /thread-reference/123456ABCDEF" should {
    "return 200" in {
      val fakeRequest = FakeRequest("GET", "/thread-reference/123456ABCDEF")
      val result      = controller.getThreadReference("123456ABCDEF")(fakeRequest)

      status(result) shouldBe Status.OK

      val json           = contentAsJson(result)
      val returnedObject = json.as[ThreadReference]

      returnedObject shouldBe threadReference
    }
  }

  "GET /thread-reference/ZZZZZZZZZZZZ" should {
    "return 404" in {
      val fakeRequest = FakeRequest("GET", "/thread-reference/ZZZZZZZZZZZZ")
      val result      = controller.getThreadReference("ZZZZZZZZZZZZ")(fakeRequest)

      status(result) shouldBe Status.NOT_FOUND

      val json = contentAsJson(result)

      (json \ "message").as[String] shouldBe
        "Thread reference [ZZZZZZZZZZZZ] not found"
    }
  }

  "GET /thread-reference/999" should {
    "return 400" in {
      val fakeRequest = FakeRequest("GET", "/thread-reference/999")
      val result      = controller.getThreadReference("999")(fakeRequest)

      status(result) shouldBe Status.BAD_REQUEST

      val json = contentAsJson(result)
      (json \ "message").as[String] shouldBe
        "Thread reference [999] must be exactly 12 characters long and contain only A-Z and 0-9"
    }
  }
}
