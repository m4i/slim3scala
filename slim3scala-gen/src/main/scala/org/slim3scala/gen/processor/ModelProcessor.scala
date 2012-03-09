package org.slim3scala.gen.processor

import com.sun.mirror.apt.AnnotationProcessorEnvironment
import com.sun.mirror.declaration.AnnotationTypeDeclaration
import com.sun.mirror.declaration.ClassDeclaration

import org.slim3.gen.desc.{ AttributeMetaDescFactory => JAttributeMetaDescFactory }
import org.slim3.gen.desc.ModelMetaDesc
import org.slim3.gen.generator.{ ModelMetaGenerator => JModelMetaGenerator }

import org.slim3scala.gen.desc.AttributeMetaDescFactory
import org.slim3scala.gen.generator.ModelMetaGenerator

class ModelProcessor(
    annotationTypeDeclarations: java.util.Set[AnnotationTypeDeclaration],
    env: AnnotationProcessorEnvironment)
    extends org.slim3.gen.processor.ModelProcessor(annotationTypeDeclarations, env) {

  protected val scalaGenerateSupport = new GenerateSupport(env)

  override protected def handleClassDeclaration(declaration: ClassDeclaration) {
    val attributeMetaDescFactory = createAttributeMetaDescFactory()
    val modelMetaDescFactory =
      createModelMetaDescFactory(attributeMetaDescFactory)
    val modelMetaDesc = modelMetaDescFactory.createModelMetaDesc(declaration)
    if (!modelMetaDesc.isError) {
      val modelMetaGenerator = createModelMetaGenerator(modelMetaDesc)
      scalaGenerateSupport.generate(modelMetaGenerator, modelMetaDesc)
    }
  }

  override protected def createAttributeMetaDescFactory:
      JAttributeMetaDescFactory = {
    new AttributeMetaDescFactory(env)
  }

  override protected def createModelMetaGenerator(
      modelMetaDesc: ModelMetaDesc): JModelMetaGenerator = {
    new ModelMetaGenerator(modelMetaDesc)
  }
}
