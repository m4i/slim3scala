package org.slim3scala.apt.mirror

import java.io.PrintWriter

import com.sun.mirror.declaration.{ ClassDeclaration => IClassDeclaration }

import scala.tools.nsc.{ Global, Settings }
import scala.tools.nsc.reporters.ConsoleReporter

import org.slim3scala.apt.mirror.declaration._
import org.slim3scala.apt.mirror.`type`._

class AptEnv(
    sourcePaths: List[String], classpath: Option[String], out: PrintWriter) {

  var error = false

  protected val settings = {
    val settings = new Settings
    classpath.foreach(settings.classpath.value_=)
    settings.stop.value = List("superaccessors")
    //settings.verbose.value = true
    settings
  }

  protected val reporter = new ConsoleReporter(settings)

  val global = new Global(settings, reporter)

  object declMaker extends {
    val env: AptEnv.this.type = AptEnv.this
  } with declaration.DeclarationMaker

  object typeMaker extends {
    val env: AptEnv.this.type = AptEnv.this
  } with `type`.TypeMaker

  object types extends {
    val env: AptEnv.this.type = AptEnv.this
  } with util.TypesImpl

  lazy val classDecls: List[IClassDeclaration] = {
    def findTopLevelClassDef(tree: global.Tree): List[global.ClassDef] = {
      tree match {
        case p: global.PackageDef => p.stats.flatMap(findTopLevelClassDef(_))
        case c: global.ClassDef   => List(c)
        case _                    => Nil
      }
    }

    val run = new global.Run

    run compile sourcePaths
    if (global.globalPhase.name != "terminal") {
      Predef.error("Error occurred while parsing")
    }
    if (reporter.hasErrors) {
      out.println("WARNING: Error occurred while typing")
    }

    val classDefs = run.units.toList.flatMap(u => findTopLevelClassDef(u.body))

    classDefs.map(d =>
      declMaker.getTypeDeclaration(d).asInstanceOf[IClassDeclaration])
  }

  def getSymbol(name: String): global.Symbol = {
    global.definitions.getClass(global.newTermName(name))
  }


  object AnnotationMirror                 extends { val env: AptEnv.this.type = AptEnv.this } with AnnotationMirrorImpl
  object AnnotationValue                  extends { val env: AptEnv.this.type = AptEnv.this } with AnnotationValueImpl
  object Declaration                      extends { val env: AptEnv.this.type = AptEnv.this } with DeclarationImpl
  object MemberDeclaration                extends { val env: AptEnv.this.type = AptEnv.this } with MemberDeclarationImpl
  object ExecutableDeclaration            extends { val env: AptEnv.this.type = AptEnv.this } with ExecutableDeclarationImpl
  object ConstructorDeclaration           extends { val env: AptEnv.this.type = AptEnv.this } with ConstructorDeclarationImpl
  object MethodDeclaration                extends { val env: AptEnv.this.type = AptEnv.this } with MethodDeclarationImpl
  object AnnotationTypeElementDeclaration extends { val env: AptEnv.this.type = AptEnv.this } with AnnotationTypeElementDeclarationImpl
  object FieldDeclaration                 extends { val env: AptEnv.this.type = AptEnv.this } with FieldDeclarationImpl
//object EnumConstantDeclaration          extends { val env: AptEnv.this.type = AptEnv.this } with EnumConstantDeclarationImpl
  object TypeDeclaration                  extends { val env: AptEnv.this.type = AptEnv.this } with TypeDeclarationImpl
  object ClassDeclaration                 extends { val env: AptEnv.this.type = AptEnv.this } with ClassDeclarationImpl
//object EnumDeclaration                  extends { val env: AptEnv.this.type = AptEnv.this } with EnumDeclarationImpl
  object InterfaceDeclaration             extends { val env: AptEnv.this.type = AptEnv.this } with InterfaceDeclarationImpl
  object AnnotationTypeDeclaration        extends { val env: AptEnv.this.type = AptEnv.this } with AnnotationTypeDeclarationImpl
//object PackageDeclaration               extends { val env: AptEnv.this.type = AptEnv.this } with PackageDeclarationImpl
  object ParameterDeclaration             extends { val env: AptEnv.this.type = AptEnv.this } with ParameterDeclarationImpl
  object TypeParameterDeclaration         extends { val env: AptEnv.this.type = AptEnv.this } with TypeParameterDeclarationImpl

  object ast {
    import org.slim3scala.apt.mirror.declaration.ast._
    object Declaration              extends { val env: AptEnv.this.type = AptEnv.this } with DeclarationImpl
    object ExecutableDeclaration    extends { val env: AptEnv.this.type = AptEnv.this } with ExecutableDeclarationImpl
    object ConstructorDeclaration   extends { val env: AptEnv.this.type = AptEnv.this } with ConstructorDeclarationImpl
    object MethodDeclaration        extends { val env: AptEnv.this.type = AptEnv.this } with MethodDeclarationImpl
    object FieldDeclaration         extends { val env: AptEnv.this.type = AptEnv.this } with FieldDeclarationImpl
    object TypeDeclaration          extends { val env: AptEnv.this.type = AptEnv.this } with TypeDeclarationImpl
    object ClassDeclaration         extends { val env: AptEnv.this.type = AptEnv.this } with ClassDeclarationImpl
    object ParameterDeclaration     extends { val env: AptEnv.this.type = AptEnv.this } with ParameterDeclarationImpl
    object TypeParameterDeclaration extends { val env: AptEnv.this.type = AptEnv.this } with TypeParameterDeclarationImpl
  }

  object TypeMirror     extends { val env: AptEnv.this.type = AptEnv.this } with TypeMirrorImpl
  object PrimitiveType  extends { val env: AptEnv.this.type = AptEnv.this } with PrimitiveTypeImpl
  object DeclaredType   extends { val env: AptEnv.this.type = AptEnv.this } with DeclaredTypeImpl
  object ClassType      extends { val env: AptEnv.this.type = AptEnv.this } with ClassTypeImpl
  object InterfaceType  extends { val env: AptEnv.this.type = AptEnv.this } with InterfaceTypeImpl
  object AnnotationType extends { val env: AptEnv.this.type = AptEnv.this } with AnnotationTypeImpl
  object VoidType       extends { val env: AptEnv.this.type = AptEnv.this } with VoidTypeImpl
}
