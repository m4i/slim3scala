package org.slim3scala.gen.generator

import java.util.Date
import scala.collection.JavaConversions._

import org.slim3.gen.{ ClassConstants => JClassConstants }
import org.slim3.gen.ProductInfo
import org.slim3.gen.datastore.ArrayType
import org.slim3.gen.datastore.BlobType
import org.slim3.gen.datastore.CollectionType
import org.slim3.gen.datastore.CorePrimitiveType
import org.slim3.gen.datastore.CoreReferenceType
import org.slim3.gen.datastore.DataType
import org.slim3.gen.datastore.EnumType
import org.slim3.gen.datastore.FloatType
import org.slim3.gen.datastore.IntegerType
import org.slim3.gen.datastore.KeyType
import org.slim3.gen.datastore.LinkedHashSetType
import org.slim3.gen.datastore.LinkedListType
import org.slim3.gen.datastore.ListType
import org.slim3.gen.datastore.LongType
import org.slim3.gen.datastore.ModelRefType
import org.slim3.gen.datastore.PrimitiveBooleanType
import org.slim3.gen.datastore.PrimitiveDoubleType
import org.slim3.gen.datastore.PrimitiveFloatType
import org.slim3.gen.datastore.PrimitiveIntType
import org.slim3.gen.datastore.PrimitiveLongType
import org.slim3.gen.datastore.PrimitiveShortType
import org.slim3.gen.datastore.SetType
import org.slim3.gen.datastore.ShortType
import org.slim3.gen.datastore.SimpleDataTypeVisitor
import org.slim3.gen.datastore.SortedSetType
import org.slim3.gen.datastore.StringType
import org.slim3.gen.datastore.TextType
import org.slim3.gen.desc.AttributeMetaDesc
import org.slim3.gen.desc.ModelMetaDesc
import org.slim3.gen.printer.Printer

import org.slim3scala.gen.ClassConstants
import org.slim3scala.gen.NotImplementedException
import org.slim3scala.gen.util.ScalaUtil

class ModelMetaGenerator(modelMetaDesc: ModelMetaDesc)
  extends org.slim3.gen.generator.ModelMetaGenerator(modelMetaDesc) {

  override protected def printPackage(printer: Printer) {
    if (modelMetaDesc.getPackageName.length != 0) {
      printer.println("package %s", modelMetaDesc.getPackageName)
      printer.println()
    }
  }

  override protected def printClass(printer: Printer) {
    printer.println(
      """//@javax.annotation.Generated(value = { "%s", "%s" }, date = "%tF %<tT")""",
      ProductInfo.getName,
      ProductInfo.getVersion,
      new Date)
    printer.println("/** */")

    printer.print("final class %s extends %s[%s]",
      modelMetaDesc.getSimpleName,
      ClassConstants.ModelMeta,
      modelMetaDesc.getModelClassName)
    printConstructor(printer)
    printer.println(" {")
    printer.println()
    printer.indent()

    printAttributeMetaFields(printer)
    printEntityToModelMethod(printer)
    printModelToEntityMethod(printer)

    printGetKeyMethod(printer)
    printSetKeyMethod(printer)
    printGetVersionMethod(printer)
    printAssignKeyToModelRefIfNecessaryMethod(printer)
    printIncrementVersionMethod(printer)
    printPrePutMethod(printer)
    printGetSchemaVersionName(printer)
    printGetClassHierarchyListName(printer)
    printIsCipherProperty(printer)

    printer.unindent()
    printer.println("}")
    printer.println()

    printObject(printer)
  }

  override protected def printConstructor(printer: Printer) {
    if (modelMetaDesc.getClassHierarchyList.isEmpty) {
      printer.print("""("%1$s", classOf[%2$s])""",
        modelMetaDesc.getKind, modelMetaDesc.getModelClassName)
    } else {
      throw new NotImplementedException
    }
  }

  override protected def printAttributeMetaFields(printer: Printer) {
      val generator = new ScalaAttributeMetaFieldsGenerator(printer)
      generator.generate()
  }

  override protected def printEntityToModelMethod(printer: Printer) {
    val generator = new ScalaEntityToModelMethodGenerator(printer)
    generator.generate()
  }

  override protected def printModelToEntityMethod(printer: Printer) {
    val generator = new ScalaModelToEntityMethodGenerator(printer)
    generator.generate()
  }

  override protected def printGetVersionMethod(printer: Printer) {
    printer.println("override protected def getVersion(model: Object): Long = {")
    printer.indent()
    val attr = modelMetaDesc.getVersionAttributeMetaDesc
    if (attr == null) {
      printer.println(
        """throw new IllegalStateException("The version property of the model(%1$s) is not defined.")""",
        modelMetaDesc.getModelClassName)
    } else {
      printer.println("val m = model.asInstanceOf[%1$s]",
        modelMetaDesc.getModelClassName)
      val dataType = attr.getDataType
      dataType.accept(
        new SimpleDataTypeVisitor[Void, Void, RuntimeException] {

          override protected def defaultAction(`type`: DataType, p: Void): Void = {
            printer.println(
              """throw new IllegalStateException("The version property of the model(%1$s) is not defined.")""",
              modelMetaDesc.getModelClassName)
            null
          }

          override def visitPrimitiveLongType(`type`: PrimitiveLongType, p: Void): Void = {
            printer.println("m.%1$s", attr.getReadMethodName)
            null
          }

          override def visitLongType(`type`: LongType, p: Void): Void = {
            printer.println(
              "m.%1$s != null ? m.%1$s.longValue : 0L",
              attr.getReadMethodName)
            null
          }

        },
        null)
    }
    printer.unindent()
    printer.println("}")
    printer.println()
  }

  override protected def printIncrementVersionMethod(printer: Printer) {
    printer.println("override protected def incrementVersion(model: Object) {")
    printer.indent()
    val attr = modelMetaDesc.getVersionAttributeMetaDesc
    if (attr != null) {
      printer.println("val m = model.asInstanceOf[%1$s]",
        modelMetaDesc.getModelClassName)
      val dataType = attr.getDataType
      dataType.accept(
        new SimpleDataTypeVisitor[Void, Void, RuntimeException] {

          override protected def defaultAction(`type`: DataType, p: Void): Void = {
            printer.println(
              """throw new IllegalStateException("The version property of the model(%1$s) is not defined.")""",
              modelMetaDesc.getModelClassName)
            null
          }

          override def visitPrimitiveLongType(`type`: PrimitiveLongType, p: Void): Void = {
            printer.println("m.%1$s(m.%2$s + 1L)",
              attr.getWriteMethodName,
              attr.getReadMethodName)
            null
          }

          override def visitLongType(`type`: LongType, p: Void): Void = {
            printer.println(
              "val version = if (m.%1$s != null) m.%1$s.longValue else 0L",
              attr.getReadMethodName)
            printer.println(
              "m.%1$s(java.lang.Long.valueOf(version + 1L))",
              attr.getWriteMethodName)
            null
          }

        },
        null)
    }
    printer.unindent()
    printer.println("}")
    printer.println()
  }

  override protected def printGetSchemaVersionName(printer: Printer) {
    printer.println(
      "override val getSchemaVersionName = \"%1$s\"",
      modelMetaDesc.getSchemaVersionName)
    printer.println()
  }

  override protected def printGetClassHierarchyListName(printer: Printer) {
    printer.println(
      "override val getClassHierarchyListName = \"%1$s\"",
      modelMetaDesc.getClassHierarchyListName)
    printer.println()
  }

  override protected def printIsCipherProperty(printer: Printer) {
    printer.println(
      "override protected def isCipherProperty(propertyName: String): Boolean = {")
    printer.indent()
    for (attr <- modelMetaDesc.getAttributeMetaDescList) {
      if (attr.isCipher) {
        printer.println(
          """if ("%1$s" == propertyName) return true""",
          attr.getName)
      }
    }
    printer.println("false")
    printer.unindent()
    printer.println("}")
    printer.println()
  }

  override protected def printGetKeyMethod(printer: Printer) {
    printer.println(
      "override protected def getKey(model: Object): %1$s = {",
      JClassConstants.Key)
    printer.indent()
    val attr = modelMetaDesc.getKeyAttributeMetaDesc
    if (attr == null) {
      printer.println(
        """throw new IllegalStateException("The key property of the model(%1$s) is not defined.")""",
        modelMetaDesc.getModelClassName)
    } else {
      printer.println("val m = model.asInstanceOf[%1$s]",
        modelMetaDesc.getModelClassName)
      printer.println("m.%1$s", attr.getReadMethodName)
    }
    printer.unindent()
    printer.println("}")
    printer.println()
  }

  override protected def printSetKeyMethod(printer: Printer) {
    printer.println(
      "override protected def setKey(model: Object, key: %1$s) {",
      JClassConstants.Key)
    printer.indent()
    val attr = modelMetaDesc.getKeyAttributeMetaDesc
    if (attr == null) {
      printer.println(
        """throw new IllegalStateException("The key property of the model(%1$s) is not defined.")""",
        modelMetaDesc.getModelClassName)
    } else {
      printer.println("validateKey(key)")
      printer.println("val m = model.asInstanceOf[%1$s]",
        modelMetaDesc.getModelClassName)
      printer.println("m.%1$s(key)", attr.getWriteMethodName)
    }
    printer.unindent()
    printer.println("}")
    printer.println()
  }

  override protected def printPrePutMethod(printer: Printer) {
    printer.println("override protected def prePut(model: Object) {")
    printer.indent()
    var first = true
    for (attr <- modelMetaDesc.getAttributeMetaDescList) {
      if (attr.getAttributeListenerClassName != null &&
          attr.getAttributeListenerClassName != JClassConstants.AttributeListener) {
        if (first) {
          printer.println("val m = model.asInstanceOf[%1$s]",
            modelMetaDesc.getModelClassName)
          first = false
        }
        printer.println(
          "m.%1$s(%4$s.slim3_%2$sAttributeListener.prePut(m.%3$s))",
          attr.getWriteMethodName,
          attr.getAttributeName,
          attr.getReadMethodName,
          modelMetaDesc.getSimpleName)
      }
    }
    printer.unindent()
    printer.println("}")
    printer.println()
  }

  override protected def printAssignKeyToModelRefIfNecessaryMethod(
      printer: Printer) {
    val generator = new ScalaAssignKeyToModelRefIfNecessaryMethodGenerator(printer)
    generator.generate()
  }

  protected def printObject(printer: Printer) {
    printer.println("/** */")
    printer.println("object %s {", modelMetaDesc.getSimpleName)

    printer.indent()
    printAttributeListenerFields(printer)
    printGetMethod(printer)
    printer.unindent()

    printer.println("}")
  }

  override protected def printAttributeListenerFields(printer: Printer) {
    for (attr <- modelMetaDesc.getAttributeMetaDescList) {
      if (attr.getAttributeListenerClassName != null &&
          attr.getAttributeListenerClassName != JClassConstants.AttributeListener) {
        printer.println(
          "private val slim3_%2$sAttributeListener = new %1$s",
          attr.getAttributeListenerClassName,
          attr.getAttributeName)
        printer.println()
      }
    }
  }

  override protected def printGetMethod(printer: Printer) {
    printer.println("val get = new %s", modelMetaDesc.getSimpleName)
  }


  protected class ScalaAttributeMetaFieldsGenerator(printer: Printer)
    extends AttributeMetaFieldsGenerator(printer) {

    override protected def defaultAction(`type`: DataType, p: AttributeMetaDesc): Void = {
      if (p.isLob || p.isUnindexed) {
        printer.println("/** */")
        printer.println(
          """final val %4$s = new %1$s[%2$s, %3$s](this, "%5$s", "%4$s", classOf[%6$s])""",
          JClassConstants.UnindexedAttributeMeta,
          modelMetaDesc.getModelClassName,
          `type`.getTypeName,
          p.getAttributeName,
          p.getName,
          ScalaUtil.getReferenceClassName(`type`.getClassName))
        printer.println()
      }
      null
    }

    override def visitCorePrimitiveType(`type`: CorePrimitiveType, p: AttributeMetaDesc): Void = {
      printer.println("/** */")
      printer.println(
        """final val %4$s = new %1$s[%2$s, %3$s](this, "%5$s", "%4$s", classOf[%6$s])""",
        JClassConstants.CoreAttributeMeta,
        modelMetaDesc.getModelClassName,
        `type`.getWrapperClassName,
        p.getAttributeName,
        p.getName,
        ScalaUtil.getReferenceClassName(`type`.getClassName))
      printer.println()
      null
    }

    override def visitCoreReferenceType(`type`: CoreReferenceType, p: AttributeMetaDesc): Void = {
      printer.println("/** */")
      if (p.isLob || p.isUnindexed) {
        printer.println(
          """final val %4$s = new %1$s[%2$s, %3$s](this, "%5$s", "%4$s", classOf[%6$s])""",
          JClassConstants.CoreUnindexedAttributeMeta,
          modelMetaDesc.getModelClassName,
          `type`.getTypeName,
          p.getAttributeName,
          p.getName,
          ScalaUtil.getReferenceClassName(`type`.getClassName))
      } else {
        printer.println(
          """final val %4$s = new %1$s[%2$s, %3$s](this, "%5$s", "%4$s", classOf[%6$s])""",
          JClassConstants.CoreAttributeMeta,
          modelMetaDesc.getModelClassName,
          `type`.getTypeName,
          p.getAttributeName,
          p.getName,
          ScalaUtil.getReferenceClassName(`type`.getClassName))
      }
      printer.println()
      null
    }

    override def visitModelRefType(`type`: ModelRefType, p: AttributeMetaDesc): Void = {
      printer.println("/** */")
      printer.println(
        """final val %5$s = new %1$s[%2$s, %3$s, %4$s](this, "%6$s", "%5$s", classOf[%7$s[_]], classOf[%8$s])""",
        JClassConstants.ModelRefAttributeMeta,
        modelMetaDesc.getModelClassName,
        `type`.getTypeName,
        `type`.getReferenceModelTypeName,
        p.getAttributeName,
        p.getName,
        ScalaUtil.getReferenceClassName(`type`.getClassName),
        `type`.getReferenceModelTypeName)
      printer.println()
      null
    }

    override def visitKeyType(`type`: KeyType, p: AttributeMetaDesc): Void = {
      printer.println("/** */")
      printer.println(
        """final val %4$s = new %1$s[%2$s, %3$s](this, "%5$s", "%4$s", classOf[%6$s])""",
        JClassConstants.CoreAttributeMeta,
        modelMetaDesc.getModelClassName,
        `type`.getTypeName,
        p.getAttributeName,
        p.getName,
        ScalaUtil.getReferenceClassName(`type`.getClassName))
      printer.println()
      null
    }

    override def visitStringType(`type`: StringType, p: AttributeMetaDesc): Void = {
      printer.println("/** */")
      if (p.isLob || p.isUnindexed) {
        printer.println(
          """final val %3$s = new %1$s[%2$s](this, "%4$s", "%3$s")""",
          JClassConstants.StringUnindexedAttributeMeta,
          modelMetaDesc.getModelClassName,
          p.getAttributeName,
          p.getName,
          ScalaUtil.getReferenceClassName(`type`.getClassName))
      } else {
        printer.println(
          """final val %3$s = new %1$s[%2$s](this, "%4$s", "%3$s")""",
          JClassConstants.StringAttributeMeta,
          modelMetaDesc.getModelClassName,
          p.getAttributeName,
          p.getName)
      }
      printer.println()
      null
    }

    override def visitBlobType(`type`: BlobType, p: AttributeMetaDesc): Void = {
      printer.println("/** */")
      printer.println(
        """final val %4$s = new %1$s[%2$s, %3$s](this, "%5$s", "%4$s", classOf[%6$s])""",
        JClassConstants.UnindexedAttributeMeta,
        modelMetaDesc.getModelClassName,
        `type`.getClassName,
        p.getAttributeName,
        p.getName,
        `type`.getClassName)
      printer.println()
      null
    }

    override def visitTextType(`type`: TextType, p: AttributeMetaDesc): Void = {
      printer.println("/** */")
      printer.println(
        """final val %4$s = new %1$s[%2$s, %3$s](this, "%5$s", "%4$s", classOf[%6$s])""",
        JClassConstants.UnindexedAttributeMeta,
        modelMetaDesc.getModelClassName,
        `type`.getClassName,
        p.getAttributeName,
        p.getName,
        `type`.getClassName)
      printer.println()
      null
    }

    override def visitCollectionType(collectionType: CollectionType, attr: AttributeMetaDesc): Void = throw new NotImplementedException
    override def visitArrayType(`type`: ArrayType , p: AttributeMetaDesc): Void = throw new NotImplementedException
  }


  protected class ScalaEntityToModelMethodGenerator(printer: Printer)
    extends EntityToModelMethodGenerator(printer) {

    override def generate() {
      printer.println(
        "override def entityToModel(entity: %2$s): %1$s = {",
        modelMetaDesc.getModelClassName,
        JClassConstants.Entity)
      printer.indent()
      if (modelMetaDesc.isAbstrct) {
        printer.println(
          """throw new %1$s("The class(%2$s) is abstract.")""",
          classOf[UnsupportedOperationException].getName,
          modelMetaDesc.getModelClassName)
      } else {
        printer.println("val model = new %1$s",
          modelMetaDesc.getModelClassName)
        for (attr <- modelMetaDesc.getAttributeMetaDescList) {
          val dataType = attr.getDataType
          dataType.accept(this, attr)
        }
        printer.println("model")
      }
      printer.unindent()
      printer.println("}")
      printer.println()
    }

    override protected def defaultAction(`type`: DataType, p: AttributeMetaDesc): Void =  {
      printer.println(
        """val _%2$s = blobToSerializable(entity.getProperty("%4$s").asInstanceOf[%3$s])""",
        `type`.getTypeName,
        p.getAttributeName,
        JClassConstants.Blob,
        p.getName)
      printer.println("model.%1$s(_%2$s)",
        p.getWriteMethodName,
        p.getAttributeName)
      null
    }

    override def visitPrimitiveBooleanType(`type`: PrimitiveBooleanType, p: AttributeMetaDesc): Void =  {
      printer.println(
        """model.%1$s(booleanToPrimitiveBoolean(entity.getProperty("%3$s").asInstanceOf[%2$s]))""",
        p.getWriteMethodName,
        `type`.getWrapperClassName,
        p.getName)
      null
    }

    override def visitPrimitiveDoubleType(`type`: PrimitiveDoubleType, p: AttributeMetaDesc): Void =  {
      printer.println(
        """model.%1$s(doubleToPrimitiveDouble(entity.getProperty("%3$s").asInstanceOf[%2$s]))""",
        p.getWriteMethodName,
        `type`.getWrapperClassName,
        p.getName)
      null
    }

    override def visitPrimitiveFloatType(`type`: PrimitiveFloatType, p: AttributeMetaDesc): Void =  {
      printer.println(
        """model.%1$s(doubleToPrimitiveFloat(entity.getProperty("%3$s").asInstanceOf[%2$s]))""",
        p.getWriteMethodName,
        JClassConstants.Double,
        p.getName)
      null
    }

    override def visitPrimitiveIntType(`type`: PrimitiveIntType, p: AttributeMetaDesc): Void =  {
      printer.println(
        """model.%1$s(longToPrimitiveInt(entity.getProperty("%3$s").asInstanceOf[%2$s]))""",
        p.getWriteMethodName,
        JClassConstants.Long,
        p.getName)
      null
    }

    override def visitPrimitiveLongType(`type`: PrimitiveLongType, p: AttributeMetaDesc): Void =  {
      printer.println(
        """model.%1$s(longToPrimitiveLong(entity.getProperty("%3$s").asInstanceOf[%2$s]))""",
        p.getWriteMethodName,
        `type`.getWrapperClassName,
        p.getName)
      null
    }

    override def visitPrimitiveShortType(`type`: PrimitiveShortType, p: AttributeMetaDesc): Void =  {
      printer.println(
        """model.%1$s(longToPrimitiveShort(entity.getProperty("%3$s").asInstanceOf[%2$s]))""",
        p.getWriteMethodName,
        JClassConstants.Long,
        p.getName)
      null
    }

    override def visitCoreReferenceType(`type`: CoreReferenceType, p: AttributeMetaDesc): Void =  {
      printer.println(
        """model.%1$s(entity.getProperty("%3$s").asInstanceOf[%2$s])""",
        p.getWriteMethodName,
        `type`.getTypeName,
        p.getName)
      null
    }

    override def visitFloatType(`type`: FloatType, p: AttributeMetaDesc): Void =  {
      printer.println(
        """model.%1$s(doubleToFloat(entity.getProperty("%3$s").asInstanceOf[%2$s]))""",
        p.getWriteMethodName,
        JClassConstants.Double,
        p.getName)
      null
    }

    override def visitIntegerType(`type`: IntegerType, p: AttributeMetaDesc): Void =  {
      printer.println(
        """model.%1$s(longToInteger(entity.getProperty("%3$s").asInstanceOf[%2$s]))""",
        p.getWriteMethodName,
        JClassConstants.Long,
        p.getName)
      null
    }

    override def visitShortType(`type`: ShortType, p: AttributeMetaDesc): Void =  {
      printer.println(
        """model.%1$s(longToShort(entity.getProperty("%3$s").asInstanceOf[%2$s]))""",
        p.getWriteMethodName,
        JClassConstants.Long,
        p.getName)
      null
    }

    override def visitStringType(`type`: StringType, p: AttributeMetaDesc): Void = {
      if (p.isLob && p.isCipher) {
        printer.println(
          """model.%1$s(decrypt(textToString(entity.getProperty("%3$s").asInstanceOf[%2$s])))""",
          p.getWriteMethodName,
          JClassConstants.Text,
          p.getName)
        null
      } else if (p.isLob) {
        printer.println(
          """model.%1$s(textToString(entity.getProperty("%3$s").asInstanceOf[%2$s]))""",
          p.getWriteMethodName,
          JClassConstants.Text,
          p.getName)
        null
      } else if (p.isCipher) {
        printer.println(
          """model.%1$s(decrypt(entity.getProperty("%3$s").asInstanceOf[%2$s]))""",
          p.getWriteMethodName,
          `type`.getTypeName,
          p.getName)
        null
      } else {
        super.visitStringType(`type`, p)
      }
    }

    override def visitEnumType(`type`: EnumType, p: AttributeMetaDesc): Void =  {
      printer.println(
        """model.%1$s(stringToEnum(classOf[%2$s], entity.getProperty("%4$s").asInstanceOf[%3$s]))""",
        p.getWriteMethodName,
        `type`.getTypeName,
        JClassConstants.String,
        p.getName)
      null
    }

    override def visitTextType(`type`: TextType, p: AttributeMetaDesc): Void = {
      if (p.isCipher) {
        printer.println(
          """model.%1$s(decrypt(entity.getProperty("%3$s").asInstanceOf[%2$s]))""",
          p.getWriteMethodName,
          `type`.getTypeName,
          p.getName)
        null
      } else {
        super.visitTextType(`type`, p)
      }
    }

    override def visitKeyType(`type`: KeyType, p: AttributeMetaDesc): Void =  {
      if (p.isPrimaryKey) {
        printer.println("model.%1$s(entity.getKey)", p.getWriteMethodName)
      } else {
        printer.println(
          """model.%1$s(entity.getProperty("%3$s").asInstanceOf[%2$s])""",
          p.getWriteMethodName,
          `type`.getTypeName,
          p.getName)
      }
      null
    }

    override def visitModelRefType(`type`: ModelRefType, p: AttributeMetaDesc): Void =  {
      printer.println("if (model.%1$s == null) {", p.getReadMethodName)
      printer.indent()
      printer.println(
        """throw new NullPointerException("The property(%1$s) is null.")""",
        p.getAttributeName)
      printer.unindent()
      printer.println("}")
      printer.println(
        """model.%1$s.setKey(entity.getProperty("%3$s").asInstanceOf[%2$s])""",
        p.getReadMethodName,
        JClassConstants.Key,
        p.getName)
      null
    }

    override def visitArrayType(`type`: ArrayType, attr: AttributeMetaDesc): Void =  throw new NotImplementedException
    override def visitListType(collectionType: ListType, attr: AttributeMetaDesc): Void =  throw new NotImplementedException
    override def visitLinkedListType(collectionType: LinkedListType, attr: AttributeMetaDesc): Void =  throw new NotImplementedException
    override def visitSetType(collectionType: SetType, attr: AttributeMetaDesc): Void =  throw new NotImplementedException
    override def visitLinkedHashSetType(collectionType: LinkedHashSetType, attr: AttributeMetaDesc): Void =  throw new NotImplementedException
    override def visitSortedSetType(collectionType: SortedSetType , attr: AttributeMetaDesc): Void =  throw new NotImplementedException
  }


  protected class ScalaModelToEntityMethodGenerator(printer: Printer)
    extends ModelToEntityMethodGenerator(printer) {

    override def generate() {
      printer.println(
        "override def modelToEntity(model: %2$s): %1$s = {",
        JClassConstants.Entity,
        JClassConstants.Object)
      printer.indent()
      if (modelMetaDesc.isAbstrct) {
        printer.println(
          """throw new %1$s("The class(%2$s) is abstract.")""",
          classOf[UnsupportedOperationException].getName,
          modelMetaDesc.getModelClassName)
      } else {
        printer.println("val m = model.asInstanceOf[%1$s]",
          modelMetaDesc.getModelClassName)
        printer.println("val entity =")
        printer.indent()
        printer.println("if (m.%1$s != null) new %2$s(m.%1$s)",
          modelMetaDesc.getKeyAttributeMetaDesc.getReadMethodName,
          JClassConstants.Entity)
        printer.println("else new %1$s(kind)", JClassConstants.Entity)
        printer.unindent()
        for (attr <- modelMetaDesc.getAttributeMetaDescList) {
          if (!attr.isPrimaryKey) {
            val dataType = attr.getDataType
            dataType.accept(this, attr)
          }
        }
        val schemaVersion = modelMetaDesc.getSchemaVersion
        if (schemaVersion > 0) {
          printer.println(
            """entity.setProperty("%1$s", %2$s)""",
            modelMetaDesc.getSchemaVersionName,
            modelMetaDesc.getSchemaVersion.asInstanceOf[AnyRef])
        }
        if (!modelMetaDesc.getClassHierarchyList.isEmpty) {
          printer.println(
            """entity.setProperty("%1$s", classHierarchyList)""",
            modelMetaDesc.getClassHierarchyListName)
        }
        printer.println("entity")
      }
      printer.unindent()
      printer.println("}")
      printer.println()
    }

    override protected def defaultAction(`type`: DataType, p: AttributeMetaDesc): Void = {
      printer.println(
        """entity.setUnindexedProperty("%1$s", serializableToBlob(m.%2$s))""",
        p.getName,
        p.getReadMethodName)
      null
    }

    override def visitCorePrimitiveType(`type`: CorePrimitiveType, p: AttributeMetaDesc): Void = {
      if (p.isUnindexed) {
        printer.println(
          """entity.setUnindexedProperty("%1$s", m.%2$s)""",
          p.getName,
          p.getReadMethodName)
      } else {
        printer.println("""entity.setProperty("%1$s", m.%2$s)""",
          p.getName,
          p.getReadMethodName)
      }
      null
    }

    override def visitCoreReferenceType(`type`: CoreReferenceType, p: AttributeMetaDesc): Void = {
      if (p.isUnindexed) {
        printer.println(
          """entity.setUnindexedProperty("%1$s", m.%2$s)""",
          p.getName,
          p.getReadMethodName)
      } else {
        printer.println("""entity.setProperty("%1$s", m.%2$s)""",
          p.getName,
          p.getReadMethodName)
      }
      null
    }

    override def visitStringType(`type`: StringType, p: AttributeMetaDesc): Void = {
      if (p.isLob && p.isCipher) {
        printer.println(
          """entity.setUnindexedProperty("%1$s", stringToText(encrypt(m.%2$s)))""",
          p.getName,
          p.getReadMethodName)
        null
      } else if (p.isLob) {
        printer.println(
          """entity.setUnindexedProperty("%1$s", stringToText(m.%2$s))""",
          p.getName,
          p.getReadMethodName)
        null
      } else if (p.isCipher) {
        printer.println(
          """entity.setProperty("%1$s", encrypt(m.%2$s))""",
          p.getName,
          p.getReadMethodName)
        null
      } else {
        super.visitStringType(`type`, p)
      }
    }

    override def visitEnumType(`type`: EnumType, p: AttributeMetaDesc): Void = {
      if (p.isUnindexed) {
        printer.println(
          """entity.setUnindexedProperty("%1$s", enumToString(m.%2$s))""",
          p.getName,
          p.getReadMethodName)
      } else {
        printer.println(
          """entity.setProperty("%1$s", enumToString(m.%2$s))""",
          p.getName,
          p.getReadMethodName)
      }
      null
    }

    override def visitTextType(`type`: TextType, p: AttributeMetaDesc): Void = {
      if (p.isCipher) {
        printer.println(
          """entity.setUnindexedProperty("%1$s", encrypt(m.%2$s))""",
          p.getName,
          p.getReadMethodName)
      } else {
        printer.println(
          """entity.setUnindexedProperty("%1$s", m.%2$s)""",
          p.getName,
          p.getReadMethodName)
      }
      null
    }

    override def visitModelRefType(`type`: ModelRefType, p: AttributeMetaDesc): Void = {
      printer.println("if (m.%1$s == null) {", p.getReadMethodName)
      printer.indent()
      printer.println(
        """throw new NullPointerException("The property(%1$s) must not be null.")""",
        p.getAttributeName)
      printer.unindent()
      printer.println("}")
      if (p.isUnindexed) {
        printer.println(
          """entity.setUnindexedProperty("%1$s", m.%2$s.getKey)""",
          p.getName,
          p.getReadMethodName)
      } else {
        printer.println(
          """entity.setProperty("%1$s", m.%2$s.getKey)""",
          p.getName,
          p.getReadMethodName)
      }
      null
    }

    override def visitArrayType(`type`: ArrayType, attr:  AttributeMetaDesc): Void =  throw new NotImplementedException
    override def visitCollectionType(`type`: CollectionType, p:  AttributeMetaDesc): Void =  throw new NotImplementedException
  }

  protected class ScalaAssignKeyToModelRefIfNecessaryMethodGenerator(
      printer: Printer)
    extends AssignKeyToModelRefIfNecessaryMethodGenerator(printer) {

    override def generate() {
      printer.println(
        "override protected def assignKeyToModelRefIfNecessary(ds: %1$s, model: %2$s) {",
        JClassConstants.DatastoreService,
        JClassConstants.Object)
      printer.indent()
      if (modelMetaDesc.isAbstrct) {
        printer.println(
          """throw new %1$s("The class(%2$s) is abstract.")""",
          classOf[UnsupportedOperationException].getName,
          modelMetaDesc.getModelClassName)
      } else {
        val found = modelMetaDesc.getAttributeMetaDescList
          .exists(_.getDataType.isInstanceOf[ModelRefType])
        if (found) {
          printer.println("val m = model.asInstanceOf[%1$s]",
            modelMetaDesc.getModelClassName)
          for (attr <- modelMetaDesc.getAttributeMetaDescList) {
            if (!attr.isPrimaryKey) {
              val dataType = attr.getDataType
              dataType.accept(this, attr)
            }
          }
        }
      }
      printer.unindent()
      printer.println("}")
      printer.println()
    }

    override def visitModelRefType(`type`: ModelRefType, p: AttributeMetaDesc): Void = {
      printer.println("if (m.%1$s == null) {", p.getReadMethodName)
      printer.indent()
      printer.println(
        """throw new NullPointerException("The property(%1$s) must not be null.")""",
        p.getAttributeName)
      printer.unindent()
      printer.println("}")
      printer.println("m.%1$s.assignKeyIfNecessary(ds)", p.getReadMethodName)
      null
    }
  }
}
