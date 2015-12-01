package com.github.playforscala.barcodes

import javax.inject.Inject
import play.api.{Application, Logger, Plugin}
import play.api.libs.concurrent.Akka
import play.api.Play.current
import akka.actor.Props


class BarcodesPlugin @Inject() (val app: Application) extends Plugin {

  override def onStart() {
    Barcodes.barcodeCache = Akka.system.actorOf(Props[BarcodeCache])
  }

  override def onStop() {
    super.onStop()
  }

  override def enabled = true
}
