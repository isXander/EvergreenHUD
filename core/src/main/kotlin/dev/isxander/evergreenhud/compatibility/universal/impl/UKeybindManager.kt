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

package dev.isxander.evergreenhud.compatibility.universal.impl

import dev.isxander.evergreenhud.compatibility.universal.KEYBIND_MANAGER
import dev.isxander.evergreenhud.utils.Keyboard

abstract class UKeybindManager {
    abstract fun registerKeybind(keybind: CustomKeybind)
}

class CustomKeybind {
    lateinit var key: Keyboard
    lateinit var name: String
    lateinit var category: String
    lateinit var executor: () -> Unit
        private set

    fun execute(lambda: () -> Unit) {
        executor = lambda
    }
}

fun registerKeybind(lambda: CustomKeybind.() -> Unit) {
    KEYBIND_MANAGER.registerKeybind(CustomKeybind().apply(lambda))
}