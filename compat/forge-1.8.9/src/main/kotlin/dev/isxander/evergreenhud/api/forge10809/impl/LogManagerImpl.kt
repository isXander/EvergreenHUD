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

package dev.isxander.evergreenhud.api.forge10809.impl

import dev.isxander.evergreenhud.api.impl.Logger as ULogger
import dev.isxander.evergreenhud.api.impl.ULogManager
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class LogManagerImpl : ULogManager() {
    override fun get(name: String): ULogger = object : ULogger {
        private val logger: Logger = LogManager.getLogger(name)

        override fun info(msg: String) = logger.info(msg)
        override fun warn(msg: String) = logger.warn(msg)
        override fun err(msg: String) = logger.error(msg)
    }
}