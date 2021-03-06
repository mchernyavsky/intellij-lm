package org.lm.navigation

import org.lm.psi.ext.LmNamedElement
import org.lm.psi.stubs.index.LmGotoClassIndex

class LmClassNavigationContributor : VcNavigationContributorBase<LmNamedElement>(
    LmGotoClassIndex.KEY,
    LmNamedElement::class.java
)
