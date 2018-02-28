package org.lm.psi.stubs

import com.intellij.psi.PsiFile
import com.intellij.psi.StubBuilder
import com.intellij.psi.stubs.DefaultStubBuilder
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.PsiFileStubImpl
import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.tree.IStubFileElementType
import org.lm.LmLanguage
import org.lm.psi.LmFile
import org.lm.psi.LmFunction
import org.lm.psi.LmModule
import org.lm.psi.LmVariable
import org.lm.psi.ext.LmCompositeElement
import org.lm.psi.impl.LmFunctionImpl
import org.lm.psi.impl.LmModuleImpl
import org.lm.psi.impl.LmVariableImpl

class LmFileStub(file: LmFile?) : PsiFileStubImpl<LmFile>(file) {

    override fun getType(): Type = Type

    object Type : IStubFileElementType<LmFileStub>(LmLanguage) {

        override fun getStubVersion(): Int = 1

        override fun getBuilder(): StubBuilder = object : DefaultStubBuilder() {
            override fun createStubForFile(file: PsiFile): StubElement<*> =
                LmFileStub(file as LmFile)
        }

        override fun serialize(stub: LmFileStub, dataStream: StubOutputStream) {
        }

        override fun deserialize(
            dataStream: StubInputStream,
            parentStub: StubElement<*>?
        ): LmFileStub = LmFileStub(null)

        override fun getExternalId(): String = "lm.file"
    }
}

fun factory(name: String): LmStubElementType<*, *> = when (name) {
    "MODULE" -> LmModuleStub.Type
    "VARIABLE" -> LmVariableStub.Type
    "FUNCTION" -> LmFunctionStub.Type
    else -> error("Unknown element $name")
}

abstract class LmDefinitionStub<DefT : LmCompositeElement>(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
    override val name: String?
) : StubBase<DefT>(parent, elementType), LmNamedStub

class LmModuleStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
    name: String?
) : LmDefinitionStub<LmModule>(parent, elementType, name) {

    object Type : LmStubElementType<LmModuleStub, LmModule>("MODULE") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            LmModuleStub(parentStub, this, dataStream.readName()?.string)

        override fun serialize(stub: LmModuleStub, dataStream: StubOutputStream) =
            with(dataStream) { writeName(stub.name) }

        override fun createPsi(stub: LmModuleStub): LmModule = LmModuleImpl(stub, this)

        override fun createStub(psi: LmModule, parentStub: StubElement<*>?): LmModuleStub =
            LmModuleStub(parentStub, this, psi.name)

        override fun indexStub(stub: LmModuleStub, sink: IndexSink) = sink.indexModule(stub)
    }
}

class LmVariableStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
    name: String?
) : LmDefinitionStub<LmVariable>(parent, elementType, name) {

    object Type : LmStubElementType<LmVariableStub, LmVariable>("VARIABLE") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            LmVariableStub(parentStub, this, dataStream.readName()?.string)

        override fun serialize(stub: LmVariableStub, dataStream: StubOutputStream) =
            with(dataStream) { writeName(stub.name) }

        override fun createPsi(stub: LmVariableStub): LmVariable = LmVariableImpl(stub, this)

        override fun createStub(psi: LmVariable, parentStub: StubElement<*>?): LmVariableStub =
            LmVariableStub(parentStub, this, psi.name)

        override fun indexStub(stub: LmVariableStub, sink: IndexSink) = sink.indexVariable(stub)
    }
}

class LmFunctionStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
    name: String?
) : LmDefinitionStub<LmFunction>(parent, elementType, name) {

    object Type : LmStubElementType<LmFunctionStub, LmFunction>("FUNCTION") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
            LmFunctionStub(parentStub, this, dataStream.readName()?.string)

        override fun serialize(stub: LmFunctionStub, dataStream: StubOutputStream) =
            with(dataStream) { writeName(stub.name) }

        override fun createPsi(stub: LmFunctionStub): LmFunction = LmFunctionImpl(stub, this)

        override fun createStub(psi: LmFunction, parentStub: StubElement<*>?): LmFunctionStub =
            LmFunctionStub(parentStub, this, psi.name)

        override fun indexStub(stub: LmFunctionStub, sink: IndexSink) = sink.indexFunction(stub)
    }
}
