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

package uk.gov.hmrc.sdecthreadinfoapialpha.query

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.mvc.QueryStringBindable
import uk.gov.hmrc.sdecthreadinfoapialpha.model.query.ThreadSummaryQuery

class ThreadSummaryQuerySpec extends AnyWordSpec with Matchers {

  private val testBindable: QueryStringBindable[ThreadSummaryQuery] = summon[QueryStringBindable[ThreadSummaryQuery]]

  private val routeParameterName: String = "query"

  private val threadOwnerKey: String = "threadOwner"
  private val threadOwnerId:  String = "PID123"

  "QueryStringBindable" must {
    "bind an empty query when no parameter is present" in {
      testBindable.bind(routeParameterName, Map.empty) mustBe Some(Right(ThreadSummaryQuery()))
    }

    "bind the thread owner when the parameter is present" in {
      val expectedResult: ThreadSummaryQuery = ThreadSummaryQuery(threadOwner = Some(threadOwnerId))

      testBindable.bind(routeParameterName, Map(threadOwnerKey -> Seq(threadOwnerId))) mustBe Some(
        Right(expectedResult)
      )
    }

    "ignore unknown parameters" in {
      testBindable.bind(routeParameterName, Map("unknown-key" -> Seq("value"))) mustBe Some(Right(ThreadSummaryQuery()))
    }

    "unbind the thread owner to 'threadOwner=value'" in {
      testBindable.unbind(
        routeParameterName,
        ThreadSummaryQuery(threadOwner = Some(threadOwnerId))
      ) mustBe s"$threadOwnerKey=$threadOwnerId"
    }

    "unbind nothing for an empty query" in {
      testBindable.unbind(routeParameterName, ThreadSummaryQuery()) mustBe ""
    }
  }

}
