package org.slim3scala.apt

import java.io.PrintWriter
import com.sun.mirror.apt.AnnotationProcessorFactory

import org.apache.commons.cli.BasicParser
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException

object Main {
  def main(args: Array[String]) {
    exit(process(args))
  }

  def process(args: Array[String]): Int = {
    processing(null, null, args)
  }

  def processing(out: PrintWriter,
                 err: PrintWriter,
                 args: Array[String]): Int = {

    val _out = if (out == null) new PrintWriter(Console.out, true) else out
    val _err = if (err == null) new PrintWriter(Console.err, true) else err

    new Main(_out, _err).process(args)
  }
}

class Main(out: PrintWriter, err: PrintWriter) {
  var classpath: Option[String] = None
  var outDir: String = _
  var factory: String = _
  var sources: List[String] = _

  def process(args: Array[String]): Int = {
    if (parseArgs(args)) {
      val _factory = Class.forName(factory)
        .newInstance.asInstanceOf[AnnotationProcessorFactory]

      new Apt(out, err).run(_factory, sources, outDir, classpath)

    } else {
      1
    }
  }

  protected def parseArgs(args: Array[String]): Boolean = {
    val parser = new BasicParser
    try {
      val line = parser.parse(options, args)

      classpath = getOption(line, "classpath").orElse(getOption(line, "cp"))
      outDir    = getOption(line, "s").getOrElse(".")
      factory   = getOption(line, "factory").get
      sources   = line.getArgs.toList

      if (sources.isEmpty) {
        printHelp("Require sources.")
        false

      } else {
        true
      }

    } catch {
      case e: ParseException =>
        printHelp(e.getMessage)
        false
    }
  }

  protected def getOption(line: CommandLine, name: String): Option[String] = {
    if (line.hasOption(name))
      Some(line.getOptionValue(name))
    else
      None
  }

  protected lazy val options: Options = {
    import org.apache.commons.cli.OptionBuilder._

    val options = new Options

    withArgName("path")
    hasArg
    withDescription("Specify where to find user class files and annotation processor factories")
    options.addOption(create("classpath"))

    withArgName("path")
    hasArg
    withDescription("Specify where to find user class files and annotation processor factories")
    options.addOption(create("cp"))

    withArgName("path")
    hasArg
    withDescription("Specify where to place processor generated source files")
    options.addOption(create("s"))

    isRequired
    withDescription("Do not compile source files to class files")
    options.addOption(create("nocompile"))

    isRequired
    withArgName("class")
    hasArg
    withDescription("Name of AnnotationProcessorFactory to use; bypasses default discovery process")
    options.addOption(create("factory"))

    options
  }

  protected def printHelp(errorMessage: String) {
    err.println(errorMessage)
    err.println()
    printHelp()
  }

  protected def printHelp() {
    val usage = "apt-scala <options> <source files>"
    val width = 100
    val formatter = new HelpFormatter
    formatter.printHelp(
      err, width, usage, null, options,
      formatter.getLeftPadding, formatter.getDescPadding, null
    )
  }
}
