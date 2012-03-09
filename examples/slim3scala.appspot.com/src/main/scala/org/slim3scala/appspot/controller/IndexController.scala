package org.slim3scala.appspot.controller

import org.slim3.controller.Navigation
import org.slim3scala.controller.Controller

class IndexController extends Controller {
  protected def run(): Navigation = {
    forward("index.html.scaml")
  }
}
