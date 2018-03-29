package org.lm.completion

import com.intellij.codeInsight.lookup.LookupElement
import org.intellij.lang.annotations.Language
import org.lm.LmTestBase
import org.lm.fileTreeFromText
import org.lm.hasCaretMarker

abstract class LmCompletionTestBase : LmTestBase() {

    protected fun doSingleCompletion(@Language("LM") before: String,
                                     @Language("LM") after: String) {
        check(hasCaretMarker(before) && hasCaretMarker(after)) {
            "Please add `<caret>` marker"
        }
        checkByText(before, after) { executeSoloCompletion() }
    }

    protected fun doSingleCompletionMultifile(@Language("LM") before: String,
                                              @Language("LM") after: String) {
        fileTreeFromText(before).createAndOpenFileWithCaretMarker()
        executeSoloCompletion()
        myFixture.checkResult(after.trimIndent())
    }

    protected fun checkNoCompletion(@Language("LM") code: String) {
        InlineFile(code).withCaret()
        noCompletionCheck()
    }

    protected fun checkNoCompletionWithMultifile(@Language("LM") code: String) {
        fileTreeFromText(code).createAndOpenFileWithCaretMarker()
        noCompletionCheck()
    }

    private fun noCompletionCheck() {
        val variants = myFixture.completeBasic()
        checkNotNull(variants) {
            val element = myFixture.file.findElementAt(myFixture.caretOffset - 1)
            "Expected zero completions, but one completion was auto inserted: `${element?.text}`."
        }
        check(variants.isEmpty()) {
            "Expected zero completions, got ${variants.size}."
        }
    }

    private fun executeSoloCompletion() {
        val variants = myFixture.completeBasic()

        if (variants != null) {
            if (variants.size == 1) {
                myFixture.type('\n')
                return
            }
            fun LookupElement.debug(): String = "$lookupString ($psiElement)"
            error("Expected a single completion, but got ${variants.size}\n"
                    + variants.joinToString("\n") { it.debug() })
        }
    }
}
