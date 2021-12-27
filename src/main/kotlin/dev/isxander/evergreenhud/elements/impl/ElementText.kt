/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.settxi.impl.string

@ElementMeta(id = "TEXT", name = "Text Display", category = "Other", description = "Displays custom text of your choosing.")
class ElementText : SimpleTextElement() {
    var text by string("Sample Text") {
        name = "Text"
        category = "Text"
        description = "The text to display."
    }

    init {
        title = ""
    }

    override fun calculateValue(): String = text
}
