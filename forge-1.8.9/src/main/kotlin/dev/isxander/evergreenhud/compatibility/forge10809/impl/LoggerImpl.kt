/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/gpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.compatibility.forge10809.impl

import dev.isxander.evergreenhud.compatibility.universal.impl.AILogger
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class LoggerImpl : AILogger() {
    private val logger: Logger = LogManager.getLogger("EvergreenHUD")

    override fun info(msg: String) = logger.info(msg)
    override fun warn(msg: String) = logger.warn(msg)
    override fun err(msg: String) = logger.error(msg)
}