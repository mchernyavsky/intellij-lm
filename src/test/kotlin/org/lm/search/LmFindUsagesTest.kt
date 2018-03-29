package org.lm.search

import com.intellij.psi.PsiElement
import org.intellij.lang.annotations.Language
import org.lm.LmTestBase
import org.lm.psi.ext.LmNamedElement

class LmFindUsagesTest : LmTestBase() {

    fun `test variable usages`() = doTestByText("""
        def x = 42
           #^
        def y = x  # - usage
    """)

    fun `test function usages`() = doTestByText("""
         def foo = fun x { 42 }
            #^
         def bar = fun y { foo }  # - usage
    """)

    fun `test parameter usages`() = doTestByText("""
        def foo = fun x {
                     #^
            x  # - usage
        }
    """)

    fun `test module usages`() = doTestByText("""
        module b {
              #^
            def _ = 42
        }
        module a {
            import b   # - usage
            include b  # - usage
        }
    """)

    fun `test binding usages`() = doTestByText("""
        def _ = let
            foo = 42
           #^
            bar = foo  # - usage
        in
            foo  # - usage
    """)

    private fun doTestByText(@Language("LM") code: String) {
        InlineFile(code)
        val source = findElementInEditor<LmNamedElement>()
        val actual = markersActual(source)
        val expected = markersFrom(code)
        assertEquals(expected.joinToString(COMPARE_SEPARATOR), actual.joinToString(COMPARE_SEPARATOR))
    }

    private fun markersActual(source: LmNamedElement) =
            myFixture.findUsages(source)
                    .filter { it.element != null }
                    .map { Pair(it.element?.line ?: -1, "usage") }

    private fun markersFrom(text: String) =
            text.split('\n')
                    .withIndex()
                    .filter { it.value.contains(MARKER) }
                    .map { Pair(it.index, "usage") }

    private companion object {
        val MARKER = "# - "
        val COMPARE_SEPARATOR = " | "
    }

    private val PsiElement.line: Int?
        get() = containingFile.viewProvider.document?.getLineNumber(textRange.startOffset)
}
