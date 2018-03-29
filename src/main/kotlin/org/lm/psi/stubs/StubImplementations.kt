package org.lm.psi.stubs

import com.intellij.psi.PsiFile
import com.intellij.psi.StubBuilder
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IStubFileElementType
import org.lm.LmLanguage
import org.lm.psi.LmFile
import org.lm.psi.LmModule
import org.lm.psi.LmVariable
import org.lm.psi.ext.LmElement
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
    else -> error("Unknown element $name")
}

abstract class LmDefinitionStub<DefT : LmElement>(
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
