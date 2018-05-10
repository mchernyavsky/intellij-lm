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
import kotlin.Int
import kotlin.String
import org.lm.LmLanguage
import org.lm.psi.LmFile
import org.lm.psi.LmModule
import org.lm.psi.LmVariable
import org.lm.psi.ext.LmElement
import org.lm.psi.impl.LmModuleImpl
import org.lm.psi.impl.LmVariableImpl

class LmFileStub(file: LmFile?) : PsiFileStubImpl<LmFile>(file) {
    override fun getType(): Type = Type
    companion object Type : IStubFileElementType<LmFileStub>(LmLanguage) {
        override fun getStubVersion(): Int = 1

        override fun getBuilder(): StubBuilder = object : DefaultStubBuilder() {
            override fun createStubForFile(file: PsiFile): StubElement<*> =
                LmFileStub(file as LmFile)
        }

        override fun serialize(stubs: LmFileStub, dataStream: StubOutputStream) {
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): LmFileStub = LmFileStub(null)

        override fun getExternalId(): String = "org.lm.file"
    }
}

fun factory(name: String): LmStubElementType<*, *> = when (name) {
    "MODULE" -> LmModuleStub.Type
    "VARIABLE" -> LmVariableStub.Type
    else -> error("Unknown element: " + name)
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
    companion object Type : LmStubElementType<LmModuleStub, LmModule>("MODULE") {
        override fun serialize(stubs: LmModuleStub, dataStream: StubOutputStream) {
            with(dataStream) { writeName(stubs.name) }
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): LmModuleStub = LmModuleStub(parentStub, this, dataStream.readName()?.string)

        override fun createPsi(stubs: LmModuleStub): LmModule = LmModuleImpl(stubs, this)

        override fun createStub(psi: LmModule, parentStub: StubElement<*>?): LmModuleStub = LmModuleStub(parentStub, this, psi.name)

        override fun indexStub(stubs: LmModuleStub, sink: IndexSink) {
            sink.indexModule(stubs)
        }
    }
}

class LmVariableStub(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String?
) : LmDefinitionStub<LmVariable>(parent, elementType, name) {
    companion object Type : LmStubElementType<LmVariableStub, LmVariable>("VARIABLE") {
        override fun serialize(stubs: LmVariableStub, dataStream: StubOutputStream) {
            with(dataStream) { writeName(stubs.name) }
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): LmVariableStub = LmVariableStub(parentStub, this, dataStream.readName()?.string)

        override fun createPsi(stubs: LmVariableStub): LmVariable = LmVariableImpl(stubs, this)

        override fun createStub(psi: LmVariable, parentStub: StubElement<*>?): LmVariableStub = LmVariableStub(parentStub, this, psi.name)

        override fun indexStub(stubs: LmVariableStub, sink: IndexSink) {
            sink.indexVariable(stubs)
        }
    }
}
