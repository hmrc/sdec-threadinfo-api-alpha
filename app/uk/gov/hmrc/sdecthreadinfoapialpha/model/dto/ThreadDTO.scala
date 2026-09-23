package uk.gov.hmrc.sdecthreadinfoapialpha.model.dto

import play.api.libs.json.{Format, Json}

case class ThreadDTO ()

object ThreadDTO {
  given format: Format[ThreadDTO] = Json.format[ThreadDTO]
}
