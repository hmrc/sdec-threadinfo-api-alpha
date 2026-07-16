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

package uk.gov.hmrc.sdecthreadinfoapialpha.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.*

class ThreadStatusSpec extends AnyWordSpec with Matchers {
  "ThreadStatus JSON format" should {

    "write Active to JSON as a string" in {
      Json.toJson(ThreadStatus.Active) shouldBe JsString("Active")
    }

    "read Active from JSON string" in {
      JsString("Active").as[ThreadStatus] shouldBe ThreadStatus.Active
    }

    "fail to read an unknown status" in {
      val json = JsString("12345678910")

      val result = json.validate[ThreadStatus]

      result.isError shouldBe true
    }
  }
}
