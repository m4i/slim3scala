package org.slim3scala.gen.desc

import com.sun.mirror.apt.AnnotationProcessorEnvironment
import com.sun.mirror.declaration.ClassDeclaration
import com.sun.mirror.declaration.FieldDeclaration
import com.sun.mirror.declaration.MethodDeclaration

import org.slim3.gen.desc.AttributeMetaDesc
import org.slim3.gen.util.TypeUtil
import org.slim3scala.gen.util.FieldDeclarationUtil

class AttributeMetaDescFactory(env: AnnotationProcessorEnvironment)
    extends org.slim3.gen.desc.AttributeMetaDescFactory(env) {

  override protected def handleMethod(
      attributeMetaDesc: AttributeMetaDesc,
      classDeclaration: ClassDeclaration,
      fieldDeclaration: FieldDeclaration,
      methodDeclarations: java.util.List[MethodDeclaration]) {

    attributeMetaDesc.setReadMethodName(
      FieldDeclarationUtil.getReadMethodName(fieldDeclaration))

    attributeMetaDesc.setWriteMethodName(
      FieldDeclarationUtil.getWriteMethodName(fieldDeclaration))
  }

  override protected def isReadMethod(
      m: MethodDeclaration,
      attributeMetaDesc: AttributeMetaDesc,
      fieldDeclaration: FieldDeclaration): Boolean = {

    if (TypeUtil.isVoid(m.getReturnType) ||
        m.getParameters.size != 0 ||
        m.getReturnType != fieldDeclaration.getType) {
      return false
    }

    m.getSimpleName ==
      FieldDeclarationUtil.getReadMethodName(fieldDeclaration)
  }

  override protected def isWriteMethod(
      m: MethodDeclaration,
      attributeMetaDesc: AttributeMetaDesc,
      fieldDeclaration: FieldDeclaration): Boolean = {

    if (!TypeUtil.isVoid(m.getReturnType) ||
         m.getParameters.size != 1) {
      return false
    }

    val parameterTypeMirror = m.getParameters.iterator.next.getType
    if (parameterTypeMirror != fieldDeclaration.getType) {
      return false
    }

    m.getSimpleName ==
      FieldDeclarationUtil.getWriteMethodName(fieldDeclaration)
  }
}
