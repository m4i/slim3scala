package org.slim3scala.tester

import org.scalatest.BeforeAndAfterEach
import org.scalatest.Suite

import org.slim3.tester.AppEngineTester

trait AppEngineSuite extends Suite with BeforeAndAfterEach {
  protected var tester: AppEngineTester = _

  override def beforeEach() {
    tester = new AppEngineTester
    tester.setUp()
  }

  override def afterEach() {
    tester.tearDown()
  }
}
