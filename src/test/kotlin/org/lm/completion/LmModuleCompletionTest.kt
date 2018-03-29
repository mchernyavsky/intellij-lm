package org.lm.completion

class LmModuleCompletionTest : LmCompletionTestBase() {

    fun `test module completion same directory`() = doSingleCompletionMultifile("""
    #- main.lm
        import my_m<caret>
    #- my_mod.lm
        def _ = 42
    """, """
        import my_mod<caret>
    """)

    fun `test module completion subdirectory`() = doSingleCompletionMultifile("""
    #- main.lm
        import my_m<caret>
    #- my_mod/mod.lm
        def _ = 42
    """, """
        import my_mod<caret>
    """)

    fun `test path`() = doSingleCompletion("""
        module foo {
            module bar {
                def frobnicate = fun x { 42 }
            }
        }
        def frobfrobfrob = fun x { 42 }
        def _ = foo.bar.frob<caret>
    """, """
        module foo {
            module bar {
                def frobnicate = fun x { 42 }
            }
        }
        def frobfrobfrob = fun x { 42 }
        def _ = foo.bar.frobnicate<caret>
    """)

    fun `test import 1`() = doSingleCompletion("""
        module foo {
            def bar = 42
        }
        import foo
        def _ = b<caret>
    """, """
        module foo {
            def bar = 42
        }
        import foo
        def _ = bar<caret>
    """)

    fun `test import 2`() = checkNoCompletion("""
        module foo {
            def bar = 42
        }
        module qwe {
            import foo
        }
        def _ = qwe.b<caret>
    """)

    fun `test include`() = doSingleCompletion("""
        module foo {
            def bar = 42
        }
        module qwe {
            include foo
        }
        def _ = qwe.b<caret>
    """, """
        module foo {
            def bar = 42
        }
        module qwe {
            include foo
        }
        def _ = qwe.bar<caret>
    """)

    fun `test import file`() = doSingleCompletionMultifile("""
    #- main.lm
        import foo
        def _ = b<caret>
    #- foo.lm
        def bar = 42
    """, """
        import foo
        def _ = bar<caret>
    """)

    fun `test include file`() = doSingleCompletionMultifile("""
    #- main.lm
        module qwe {
            include foo
        }
        def _ = qwe.b<caret>
    #- foo.lm
        def bar = 42
    """, """
        import foo
        def _ = qwe.bar<caret>
    """)
}
