package org.lm.refactoring

import org.intellij.lang.annotations.Language
import org.lm.LmTestBase
import org.lm.psi.LmQualifiedIdPart
import org.nbkit.common.psi.ext.descendantsOfType

class RenameTest : LmTestBase() {

    fun `test rename function parameter`() = doTest("spam", """
        def _ = fun <caret>xxx { xxx }
    """, """
        def _ = fun <caret>spam { spam }
    """)

    fun `test rename variable`() = doTest("spam", """
        module foo {
            def <caret>bar = 42
            def x = bar
        }
        def _ = foo.bar
    """, """
        module foo {
            def <caret>spam = 42
            def x = spam
        }
        def _ = foo.spam
    """)

    fun `test rename binding in sequential let`() = doTest("spam", """
        def _ = let
            foo = bar
            bar = <caret>foo
        in
            _
    """, """
        def _ = let
            spam = bar
            bar = <caret>spam
        in
            _
    """)

    fun `test rename binding in recursive let`() = doTest("spam", """
        def _ = letrec
            foo = <caret>foo
        in
            _
    """, """
        def _ = letrec
            spam = <caret>spam
        in
            _
    """)

    fun `test rename binding in parallel let`() = doTest("spam", """
        def _ = letpar
            <caret>foo = 42
        in
            foo
    """, """
        def _ = letpar
            <caret>spam = 42
        in
            spam
    """)

    fun `test rename module`() = doTest("spam", """
        module <caret>foo {
            def bar = 42
        }
        include foo
        def _ = foo.bar
    """, """
        module <caret>spam {
            def bar = 42
        }
        include spam
        def _ = spam.bar
    """)

    fun `test rename file`() = checkByDirectory("""
    #- main.lm
        import foo
    #- foo.lm
        def bar = 42
    """, """
    #- main.lm
        import spam
    #- spam.lm
        def bar = 42
    """) {
        val file = myFixture.configureFromTempProjectFile("foo.lm")
        myFixture.renameElement(file, "spam.lm")
    }

    fun `test rename import`() = checkByDirectory("""
    #- main.lm
        import foo
    #- foo.lm
        def bar = 42
    """, """
    #- main.lm
        import spam
    #- spam.lm
        def bar = 42
    """) {
        val id = myFixture.configureFromTempProjectFile("main.lm").descendantsOfType<LmQualifiedIdPart>().single()
        check(id.referenceName == "foo")
        val file = id.reference?.resolve()!!
        myFixture.renameElement(file, "spam")
    }

    fun `test rename directory`() = checkByDirectory("""
    #- main.lm
        import sub
    #- sub/foo.lm
        def bar = 42
    """, """
    #- main.lm
        import spam
    #- spam/foo.lm
        def bar = 42
    """) {
        val dir = myFixture.configureFromTempProjectFile("sub")
        myFixture.renameElement(dir, "spam")
    }

    private fun doTest(
            newName: String,
            @Language("LM") before: String,
            @Language("LM") after: String
    ) {
        InlineFile(before).withCaret()
        myFixture.renameElementAtCaret(newName)
        myFixture.checkResult(after)
    }
}
