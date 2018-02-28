package org.lm.psi.ext

interface LmReferenceElement : LmCompositeElement {
    val referenceNameElement: LmCompositeElement?
    val referenceName: String?
}
