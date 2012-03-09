package org.slim3scala.gen.processor

import com.sun.mirror.apt.AnnotationProcessor
import com.sun.mirror.apt.AnnotationProcessorEnvironment
import com.sun.mirror.declaration.AnnotationTypeDeclaration

class ModelProcessorFactory
    extends org.slim3.gen.processor.ModelProcessorFactory {

  override def getProcessorFor(
      annotationTypeDeclarations: java.util.Set[AnnotationTypeDeclaration],
      env: AnnotationProcessorEnvironment):
      AnnotationProcessor = {

    new ModelProcessor(annotationTypeDeclarations, env)
  }
}
