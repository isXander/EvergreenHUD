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

abstract class ULogManager : Logger {
    abstract operator fun get(name: String): Logger

    val default = get("EvergreenHUD")
    override fun info(msg: String) = default.info(msg)
    override fun warn(msg: String) = default.warn(msg)
    override fun err(msg: String) = default.err(msg)
}

interface Logger {
    fun info(msg: String)
    fun warn(msg: String)
    fun err(msg: String)
}