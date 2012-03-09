package org.slim3scala.sbt

import _root_.sbt.ExitHook
import _root_.sbt.Path
import _root_.sbt.Process

import _root_.sbt.ExitHooksWrapper

trait AppEngineDevAppserver extends AppEngineBase {
  lazy val devAppserverOptions = propertyOptional[String]("")
  lazy val jrebelPath          = propertyOptional[EnvPath](null)

  lazy val devAppserverRun     = devAppserverRunAction
  lazy val devAppserverStop    = devAppserverStopAction
  lazy val devAppserverRestart = devAppserverRestartAction

  def devAppserverRunAction = task {
    args => task {
      devAppserverInstance(args)
    } dependsOn(prepareWebapp)
  } describedAs("Starts the dev_appserver.")

  def devAppserverStopAction = task {
    devAppserverInstance.stop()
  } describedAs("Stops the dev_appserver.")

  def devAppserverRestartAction = task {
    args => task {
      devAppserverInstance.stop()
      Thread.sleep(1000) // FIXME
      devAppserverInstance(args)
    } dependsOn(prepareWebapp)
  } describedAs("Restarts the dev_appserver.")

  lazy val devAppserverInstance = new DevAppserverRunner

  lazy val devAppserverJvmOptions: List[String] =
    devAppserverjvmOptionsForJrebel

  lazy val devAppserverjvmOptionsForJrebel: List[String] =
    jrebelPath.get match {
      case Some(path) if path != null =>
        if (path.asFile.isFile) {
          List("-javaagent:" + path.absolutePath, "-noverify")
        } else {
          log.error("No such file - jrebel.path = \"%s\"".format(path))
          Nil
        }
      case _ => Nil
    }

  lazy val devAppserverOptionsByProperty: List[String] =
    devAppserverOptions.get match {
      case Some(s) if !s.trim.isEmpty => s.trim.split(" +").toList
      case _                          => Nil
    }

  class DevAppserverRunner extends Runnable with ExitHook {
    ExitHooksWrapper.register(this)
    def name = "dev_appserver-shutdown"
    def runBeforeExiting() { stop(true) }

    lazy val java =
      (Path.fileProperty("java.home") / "bin" / "java").absolutePath

    lazy val jvmArguments =
      "-ea" :: "-cp" :: gaeToolsJar.absolutePath ::
      ("-javaagent:" + gaeAgentJar.absolutePath) ::
      devAppserverJvmOptions :::
      "com.google.appengine.tools.development.DevAppServerMain" :: Nil

    protected var running: Option[Process] = None

    def apply(options: Seq[String]): Option[String] =
      if (running.isDefined)
        Some("dev_appserver is already running.")
      else {
        val args = devAppserverOptionsByProperty :::
          options.toList ::: temporaryWarPath.absolutePath :: Nil
        log.info("exec: dev_appserver.sh " + args.mkString(" "))
        val builder = Process(java :: jvmArguments ::: args)
        running = Some(builder.run())
        new Thread(this).start()
        log.info("dev_appserver started.")
        None
      }

    def stop(): Option[String] = stop(false)
    def stop(suppressWarnings: Boolean): Option[String] = {
      running match {
        case Some(process) =>
          process.destroy
          running = None
          log.info("dev_appserver stopped.")
        case _ =>
          if (!suppressWarnings) log.warn("dev_appserver not running.")
      }
      None
    }

    def run() {
      running.foreach(_.exitValue())
      running = None
    }
  }
}
