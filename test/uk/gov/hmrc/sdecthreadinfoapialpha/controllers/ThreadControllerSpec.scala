package uk.gov.hmrc.sdecthreadinfoapialpha.controllers

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.ExecutionContext

class ThreadControllerSpec extends AnyWordSpec with Matchers {
  given ec:ExecutionContext = scala.concurrent.ExecutionContext.global

  private val controller = new ThreadController()
  
  
}
