package org.lm.psi.ext

interface LmReferenceElement : LmElement {
    val referenceNameElement: LmElement?
    val referenceName: String?
}
