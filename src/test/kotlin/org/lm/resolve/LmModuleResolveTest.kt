package org.lm.resolve

class LmModuleResolveTest : LmResolveTestBase() {

    fun `test modules and imports 1`() = checkByCode("""
        def c = 4
        module A {
            import B
                  #^
            def a = (b + c)
        }
        module B {
              #X
            import C
            def b = 0
        }
        module C {
            def b = 1
            def c = b
        }
    """)

    fun `test modules and imports 2`() = checkByCode("""
        def c = 4
        module A {
            import B
            def a = (b + c)
                    #^
        }
        module B {
            import C
            def b = 0
               #X
        }
        module C {
            def b = 1
            def c = b
        }
    """)

    fun `test modules and imports 3`() = checkByCode("""
        def c = 4
           #X
        module A {
            import B
            def a = (b + c)
                        #^
        }
        module B {
            import C
            def b = 0
        }
        module C {
            def b = 1
            def c = b
        }
    """)

    fun `test modules and imports 4`() = checkByCode("""
        def c = 4
        module A {
            import B
            def a = (b + c)
        }
        module B {
            import C
            def b = 0
        }
        module C {
            def b = 1
               #X
            def c = b
                   #^
        }
    """)

    fun `test parent vs import`() = checkByCode("""
        def a = 1
        module A {
            def a = 2
               #X
            def b = 3
        }
        module C {
            import A
            def b = a
                   #^
            def c = b
        }
    """)

    fun `test parent of import`() = checkByCode("""
        def a = 1
        module B { }
        module C {
            def a = 2
               #X
            module D {
                import B
                def e = a
                       #^
            }
        }
    """)

    fun `test self import`() = checkByCode("""
        module A {
            module A {
                def a = 1
            }
        }
        import A
        def b = a
               #^ unresolved
    """)

    fun `test anomalous resolution 1`() = checkByCode("""
        module A {
            module B {
                def x = 1
            }
        }
        module B {
            module A {
                def y = 2
            }
        }
        module C {
            import A
            import B
            def z = (x + y)
                    #^ unresolved
        }
    """)

    fun `test anomalous resolution 2`() = checkByCode("""
        module A {
            module B {
                def x = 1
            }
        }
        module B {
            module A {
                def y = 2
            }
        }
        module C {
            import A
            import B
            def z = (x + y)
                        #^ unresolved
        }
    """)

    fun `test include 1`() = checkByCode("""
        module A {
              #X
            def x = 3
        }
        module B {
            include A
                   #^
            def x = 6
            def z = x
        }
    """)

    fun `test include 2`() = checkByCode("""
        module A {
            def x = 3
        }
        module B {
            include A
            def x = 6
               #X
            def z = x
                   #^
        }
    """)
}
