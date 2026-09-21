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

package uk.gov.hmrc.sdecthreadinfoapialpha

import play.api.inject.{Binding, Module as AppModule}
import play.api.{Configuration, Environment}
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.repository.{SDECRecipientRepositoryAlgebra, SDECThreadDetailRepositoryAlgebra, SDECThreadRepositoryAlgebra}
import uk.gov.hmrc.sdecthreadinfoapialpha.hcp.stub.{SDECRecipientRepository, SDECThreadDetailRepository, SDECThreadRepository}
import uk.gov.hmrc.sdecthreadinfoapialpha.repository.*
import uk.gov.hmrc.sdecthreadinfoapialpha.service.*
import uk.gov.hmrc.sdecthreadinfoapialpha.stubs.ThreadReferenceRepository

import java.time.Clock

class Module extends AppModule:

  override def bindings(
    environment:   Environment,
    configuration: Configuration
  ): Seq[Binding[?]] =
    Seq(
      bind[Clock].toInstance(Clock.systemDefaultZone()),
      bind[ThreadReferenceRepositoryAlgebra]
        .to[ThreadReferenceRepository],
      bind[SDECThreadRepositoryAlgebra].to[SDECThreadRepository],
      bind[SDECThreadDetailRepositoryAlgebra].to[SDECThreadDetailRepository],
      bind[SDECRecipientRepositoryAlgebra].to[SDECRecipientRepository],
      bind[ThreadReferenceServiceAlgebra]
        .to[ThreadReferenceService],
      bind[ThreadSummaryServiceAlgebra]
        .to[ThreadSummaryService]
    )
