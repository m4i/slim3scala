package org.slim3scala.apt

import java.io.PrintWriter
import scala.collection.JavaConversions._

import com.sun.mirror.apt.AnnotationProcessorFactory
import com.sun.mirror.declaration.AnnotationTypeDeclaration

import org.slim3scala.apt.mirror.AptEnv
import org.slim3scala.apt.mirror.apt.AnnotationProcessorEnvironmentImpl

class Apt(out: PrintWriter, err: PrintWriter) {
  def run(
      factory: AnnotationProcessorFactory,
      sourcePaths: List[String], outDir: String, classpath: Option[String]): Int = {

    val env = new AptEnv(sourcePaths, classpath, err)

    val mirrorEnv =
      new AnnotationProcessorEnvironmentImpl(env, env.classDecls, outDir, err)

    val annotationTypeDecls =
      factory.supportedAnnotationTypes
        .map(name =>
          env.declMaker.getTypeDeclaration(env.getSymbol(name))
            .asInstanceOf[AnnotationTypeDeclaration]
        ).toSet

    val processor = factory.getProcessorFor(annotationTypeDecls, mirrorEnv)

    processor.process()

    if (env.error) 1 else 0
  }
}
