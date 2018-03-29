package org.lm.resolve

class LmResolveTest : LmResolveTestBase() {

    fun `test function parameter`() = checkByCode("""
        def _ = fun n {
                   #X
            n
           #^
        }
    """)

    fun `test recursion`() = checkByCode("""
        def foo = fun n {
           #X
            (foo (n - 1))
            #^
        }
    """)

    fun `test variables`() = checkByCode("""
        def x = 42
           #X
        def _ = x
               #^
    """)

    fun `test closure`() = checkByCode("""
        def x = 42
           #X
        def foo = fun n {
             x
            #^
        }
    """)

    fun `test global scope 1`() = checkByCode("""
        def a = 0
           #X
        def b = (a + c)
                #^
        def b = (b + d)
        def c = 0
    """)

    fun `test global scope 2`() = checkByCode("""
        def a = 0
        def b = (a + c)
                    #^
        def b = (b + d)
        def c = 0
           #X
    """)

    fun `test global scope 3`() = checkByCode("""
        def a = 0
        def b = (a + c)
        def b =
           #X
            (b + d)
            #^
        def c = 0
    """)

    fun `test global scope 4`() = checkByCode("""
        def a = 0
        def b = (a + c)
        def b = (b + d)
                    #^ unresolved
        def c = 0
    """)

    fun `test lexical scoping 1`() = checkByCode("""
        def f =
            fix f {
                fun n {
                   #X
                    ifz n then 1
                       #^
                    else (n * (f (n - 1)))
                }
            }
        def n = (f 5)
    """)

    fun `test lexical scoping 2`() = checkByCode("""
        def f =
            fix f {
               #X
                fun n {
                    ifz n then 1
                    else (n * (f (n - 1)))
                              #^
                }
            }
        def n = (f 5)
    """)

    fun `test lexical scoping 3`() = checkByCode("""
        def f =
           #X
            fix f {
                fun n {
                    ifz n then 1
                    else (n * (f (n - 1)))
                }
            }
        def n = (f 5)
                #^
    """)

    fun `test sequential let 1`() = checkByCode("""
        def a = 0
        def b = 1
        def c = 2
           #X
        def r = let
            a = c
               #^
            b = a
            c = b
        in
            (a + (b + c))
    """)

    fun `test sequential let 2`() = checkByCode("""
        def a = 0
        def b = 1
        def c = 2
        def r = let
            a = c
           #X
            b = a
               #^
            c = b
        in
            (a + (b + c))
    """)

    fun `test sequential let 3`() = checkByCode("""
        def a = 0
        def b = 1
        def c = 2
        def r = let
            a = c
            b = a
           #X
            c = b
               #^
        in
            (a + (b + c))
    """)

    fun `test sequential let 4`() = checkByCode("""
        def a = 0
        def b = 1
        def c = 2
        def r = let
            a = c
           #X
            b = a
            c = b
        in
            (a + (b + c))
            #^
    """)

    fun `test let recursive binding 1`() = checkByCode("""
        def a = 0
        def b = 1
        def c = 2
        def r = letrec
            a = c
               #^
            b = a
            c = b
           #X
        in
            (a + (b + c))
    """)

    fun `test let recursive binding 2`() = checkByCode("""
        def a = 0
        def b = 1
        def c = 2
        def r = letrec
            a = c
           #X
            b = a
               #^
            c = b
        in
            (a + (b + c))
    """)

    fun `test let recursive binding 3`() = checkByCode("""
        def a = 0
        def b = 1
        def c = 2
        def r = letrec
            a = c
            b = a
           #X
            c = b
               #^
        in
            (a + (b + c))
    """)

    fun `test let recursive binding 4`() = checkByCode("""
        def a = 0
        def b = 1
        def c = 2
        def r = letrec
            a = c
           #X
            b = a
            c = b
        in
            (a + (b + c))
            #^
    """)

    fun `test let parallel binding 1`() = checkByCode("""
        def a = 0
        def b = 1
        def c = 2
           #X
        def r = letpar
            a = c
               #^
            b = a
            c = b
        in
            (a + (b + c))
    """)

    fun `test let parallel binding 2`() = checkByCode("""
        def a = 0
           #X
        def b = 1
        def c = 2
        def r = letpar
            a = c
            b = a
               #^
            c = b
        in
            (a + (b + c))
    """)

    fun `test let parallel binding 3`() = checkByCode("""
        def a = 0
        def b = 1
           #X
        def c = 2
        def r = letpar
            a = c
            b = a
            c = b
               #^
        in
            (a + (b + c))
    """)

    fun `test let parallel binding 4`() = checkByCode("""
        def a = 0
        def b = 1
        def c = 2
        def r = letpar
            a = c
           #X
            b = a
            c = b
        in
            (a + (b + c))
            #^
    """)

    fun `test partially qualified name 1`() = checkByCode("""
        module B {
             module C {
                def c = (D.f 3)
                          #^
             }
             module D {
                def f = 1
                   #X
             }
        }
    """)

    fun `test partially qualified name 2`() = checkByCode("""
        module B {
             module C {
                def c = (D.f 3)
                        #^
             }
             module D {
                   #X
                def f = 1
             }
        }
    """)
}
