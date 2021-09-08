/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.api.world
import dev.isxander.evergreenhud.elements.ElementMeta
import dev.isxander.evergreenhud.elements.type.SimpleTextElement

@ElementMeta(id = "DAY_COUNTER", name = "Day Counter", description = "Displays the current day in the world.", category = "World")
class ElementDay : SimpleTextElement() {
    override var title: String = "Day"

    override fun calculateValue(): String {
        if (world.isNull) return "0"
        return (world.time / 24000L).toString()
    }

}