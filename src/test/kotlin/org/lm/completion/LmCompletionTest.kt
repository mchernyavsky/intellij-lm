package org.lm.completion

class LmCompletionTest : LmCompletionTestBase() {

    fun `test module`() = doSingleCompletion("""
        module frobnicate { }
        def _ = frob<caret>
    """, """
        module frobnicate { }
        def _ = frobnicate<caret>
    """)

    fun `test variable`() = doSingleCompletion("""
        def frobnicate = 42
        def _ = frob<caret>
    """, """
        def frobnicate = 42
        def _ = frobnicate<caret>
    """)

    fun `test function parameter`() = doSingleCompletion("""
        def _ = fun frobnicate { frob<caret> }
    """, """
        def _ = fun frobnicate { frobnicate<caret> }
    """)

    fun `test sequential let 1`() = doSingleCompletion("""
        def _ = let
            foo = bar
            bar = f<caret>
        in
            _
    """, """
        def _ = let
            foo = bar
            bar = foo<caret>
        in
            _
    """)

    fun `test sequential let 2`() = checkNoCompletion("""
        def _ = let
            foo = b<caret>
            bar = foo
        in
            _
    """)

    fun `test recursive let`() = doSingleCompletion("""
        def _ = letrec
            foo = f<caret>
        in
            _
    """, """
        def _ = letrec
            foo = foo<caret>
        in
            _
    """)

    fun `test parallel let 1`() = doSingleCompletion("""
        def _ = letpar
            foo = 42
        in
            f<caret>
    """, """
        def _ = letpar
            foo = 42
        in
            foo<caret>
    """)

    fun `test parallel let 2`() = checkNoCompletion("""
        def _ = letpar
            foo = f<caret>
        in
            _
    """)
}
