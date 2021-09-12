/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.api.impl

import dev.isxander.evergreenhud.api.keybindManager
import dev.isxander.evergreenhud.utils.Input

abstract class UKeybindManager {
    abstract fun registerKeybind(keybind: CustomKeybind)
}

class CustomKeybind {
    lateinit var key: Input
    lateinit var name: String
    lateinit var category: String

    var keyDown = false
    var pressed = false

    lateinit var onDown: () -> Unit
        private set
    lateinit var onUp: () -> Unit
        private set

    fun onDown(lambda: () -> Unit) {
        onDown = lambda
    }
    fun onUp(lambda: () -> Unit) {
        onUp = lambda
    }
}

fun registerKeybind(lambda: CustomKeybind.() -> Unit): CustomKeybind {
    val keybind = CustomKeybind().apply(lambda)
    keybindManager.registerKeybind(keybind)
    return keybind
}