package org.slim3scala.tester

import org.scalatest.BeforeAndAfterEach
import org.scalatest.Suite

import org.slim3.tester.ControllerTester

trait ControllerSuite extends Suite with BeforeAndAfterEach {
  protected var tester: ControllerTester = _

  override def beforeEach() {
    tester = new ControllerTester(getClass)
    tester.setUp()
  }

  override def afterEach() {
    tester.tearDown()
  }
}
