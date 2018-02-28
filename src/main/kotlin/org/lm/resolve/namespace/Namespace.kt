package org.lm.resolve.namespace

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import org.lm.psi.ext.LmCompositeElement
import org.lm.psi.ext.LmNamedElement

interface Namespace {
    val symbols: Set<LookupElement>

    fun resolve(name: String): LmCompositeElement?
}

object EmptyNamespace : Namespace {
    override val symbols: Set<LookupElement> = emptySet()

    override fun resolve(name: String): LmCompositeElement? = null
}

class SimpleNamespace : Namespace {
    private val items: MutableMap<String, LmCompositeElement> = mutableMapOf()

    override val symbols: Set<LookupElement>
        get() = items.values.map {
            if (it is LmNamedElement) {
                LookupElementBuilder.createWithIcon(it)
            } else {
                LookupElementBuilder.create(it)
            }
        }.toSet()

    override fun resolve(name: String): LmCompositeElement? = items[name]

    fun put(definition: LmNamedElement?): LmCompositeElement? =
        definition?.name?.let { items.put(it, definition) }

    fun put(name: String, definition: LmCompositeElement): LmCompositeElement? =
        items.put(name, definition)

    fun putAll(other: SimpleNamespace) = items.putAll(other.items)
}
