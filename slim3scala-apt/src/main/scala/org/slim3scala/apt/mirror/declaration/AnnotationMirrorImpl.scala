package org.slim3scala.apt.mirror
package declaration

import java.util.LinkedHashMap

import com.sun.mirror.declaration.AnnotationMirror
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration
import com.sun.mirror.declaration.AnnotationValue
import com.sun.mirror.`type`.AnnotationType
import com.sun.mirror.util.SourcePosition

trait AnnotationMirrorImpl {
  val env: AptEnv

class Impl(annotationInfo: env.global.AnnotationInfo)
    extends AnnotationMirror {

  def getAnnotationType: AnnotationType = {
    env.typeMaker.getType(annotationInfo.atp).asInstanceOf[AnnotationType]
  }

  def getElementValues:
      java.util.Map[AnnotationTypeElementDeclaration, AnnotationValue] = {

    if (annotationInfo.args.nonEmpty) {
      throw new NotImplementedException
    }

    val map = new LinkedHashMap[
      AnnotationTypeElementDeclaration, AnnotationValue]

    for ((name, value) <- annotationInfo.assocs) {
      annotationInfo.atp.decls.find(_.name == name) match {
        case Some(method) =>
          map.put(
            env.declMaker.getExecutableDeclaration(method)
              .asInstanceOf[AnnotationTypeElementDeclaration],
            env.declMaker.getAnnotationValue(value, annotationInfo.pos)
          )
        case _ =>
          error(name + " is not found.")
      }
    }

    map
  }

  def getPosition: SourcePosition = {
    new util.SourcePositionImpl(annotationInfo.pos)
  }
}

}
