package org.lm.psi.stubs.index

import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import org.lm.psi.ext.LmNamedElement
import org.lm.psi.stubs.LmFileStub

class LmNamedElementIndex : StringStubIndexExtension<LmNamedElement>() {

    override fun getVersion(): Int = LmFileStub.Type.stubVersion

    override fun getKey(): StubIndexKey<String, LmNamedElement> = KEY

    companion object {
        val KEY: StubIndexKey<String, LmNamedElement> =
            StubIndexKey.createIndexKey(LmNamedElementIndex::class.java.canonicalName)
    }
}
