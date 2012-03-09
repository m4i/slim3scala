package org.slim3scala.apt.mirror
package apt

import java.io.PrintWriter
import java.util.{ Collection, HashMap }
import scala.collection.JavaConversions._

import com.sun.mirror.apt.AnnotationProcessorEnvironment
import com.sun.mirror.apt.AnnotationProcessorListener
import com.sun.mirror.apt.Filer
import com.sun.mirror.apt.Messager
import com.sun.mirror.declaration.AnnotationTypeDeclaration
import com.sun.mirror.declaration.Declaration
import com.sun.mirror.declaration.PackageDeclaration
import com.sun.mirror.declaration.TypeDeclaration
import com.sun.mirror.util.{ Declarations, Types }

class AnnotationProcessorEnvironmentImpl(
    env: AptEnv, typeDecls: List[TypeDeclaration],
    outDir: String, out: PrintWriter)
    extends AnnotationProcessorEnvironment {

  def getDeclarationsAnnotatedWith(a: AnnotationTypeDeclaration): Collection[Declaration] = {
    typeDecls.filter(_.getAnnotationMirrors.exists(_.getAnnotationType.getDeclaration == a))
  }

  lazy val getFiler: Filer = {
    new FilerImpl(outDir)
  }

  lazy val getMessager: Messager = {
    new MessagerImpl(env, out)
  }

  def getOptions: java.util.Map[String, String] = {
    new HashMap[String, String]
  }

  def getTypeDeclaration(name: String): TypeDeclaration = {
    env.declMaker.getTypeDeclaration(name)
  }

  def getTypeUtils: Types = {
    env.types
  }

  def addListener(listner: AnnotationProcessorListener) { throw new NotImplementedException }
  def getDeclarationUtils: Declarations = throw new NotImplementedException
  def getPackage(name: String): PackageDeclaration = throw new NotImplementedException
  def getSpecifiedTypeDeclarations: Collection[TypeDeclaration] = throw new NotImplementedException
  def getTypeDeclarations: Collection[TypeDeclaration] = throw new NotImplementedException
  def removeListener(listner: AnnotationProcessorListener) { throw new NotImplementedException }
}
