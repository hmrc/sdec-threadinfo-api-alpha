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

package uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp

import play.api.libs.json.{Format, JsError, JsString, JsSuccess, Reads, Writes}
import uk.gov.hmrc.sdecthreadinfoapialpha.model.dto.ThreadStatus

enum SRSRole {
  case Supervisor
  case CaseWorker
}

object SRSRole {
  given Format[SRSRole] = Format(
    Reads {
      case JsString(value) =>
        SRSRole.values
          .find(_.toString == value)
          .map(JsSuccess(_))
          .getOrElse(JsError(s"Unknown SRS Role: $value"))

      case _ => JsError("SRS Role must be a string")
    },
    Writes(role => JsString(role.toString))
  )
}
