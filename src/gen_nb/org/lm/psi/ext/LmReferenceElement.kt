package org.lm.psi.ext

import kotlin.String

interface LmReferenceElement : LmElement {
    val referenceNameElement: LmElement?

    val referenceName: String?
}
