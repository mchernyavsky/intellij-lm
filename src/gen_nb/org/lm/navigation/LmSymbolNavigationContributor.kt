package org.lm.navigation

import org.lm.psi.ext.LmNamedElement
import org.lm.psi.stubs.index.LmNamedElementIndex

class LmSymbolNavigationContributor : LmNavigationContributorBase<LmNamedElement>(LmNamedElementIndex.KEY, LmNamedElement::class.java)
