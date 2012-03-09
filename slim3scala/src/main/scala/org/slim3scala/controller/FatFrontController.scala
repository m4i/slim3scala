package org.slim3scala.controller

import org.slim3.controller.FrontController

import org.slim3scala.controller.flash.FlashFilter

class FatFrontController extends FrontController with FlashFilter
