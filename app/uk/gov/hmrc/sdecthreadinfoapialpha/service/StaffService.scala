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

import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.{SDECStaffRepositoryAlgebra, StaffRoleRepositoryAlgebra}
import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.SRSRole

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class StaffService @Inject() (
  staffRepository:     SDECStaffRepositoryAlgebra,
  staffRoleRepository: StaffRoleRepositoryAlgebra
)(using ExecutionContext)
    extends StaffServiceAlgebra {

  override def validateAccess(
    pid:  String,
    role: String
  ): Future[Boolean] =

    SRSRole.values.find(_.toString == role) match {

      case Some(requestedRole) =>
        staffRepository.findByPid(pid).flatMap {

          case Some(staff) =>
            staffRoleRepository
              .findByStaffId(staff.id)
              .map(_.exists(_.srsRole == requestedRole))

          case None =>
            Future.successful(false)
        }

      case None =>
        Future.successful(false)
    }
}
