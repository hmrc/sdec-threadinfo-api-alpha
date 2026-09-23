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

package uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository

import uk.gov.hmrc.sdecthreadinfoapialpha.model.hcp.SDECThread

import scala.concurrent.Future

trait SDECThreadRepositoryAlgebra {

  def findById(id: Long): Future[Option[SDECThread]]

  def findByReference(reference: String): Future[Option[SDECThread]]

  def findByCreatedBy(staffId: Long): Future[Seq[SDECThread]]

  def findByRecipientId(recipientId: Long): Future[Seq[SDECThread]]

  def findAll(): Future[Seq[SDECThread]]

  def insert(thread: SDECThread): Future[Long]

  def update(thread: SDECThread): Future[Int]

  def delete(id: Long): Future[Int]
}