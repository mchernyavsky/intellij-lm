package org.lm.navigation

import org.lm.psi.ext.LmNamedElement
import org.lm.psi.stubs.index.LmGotoClassIndex

class LmClassNavigationContributor : LmNavigationContributorBase<LmNamedElement>(LmGotoClassIndex.KEY, LmNamedElement::class.java)
