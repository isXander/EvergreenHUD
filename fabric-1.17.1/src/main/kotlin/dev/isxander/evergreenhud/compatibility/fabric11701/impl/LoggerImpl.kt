package dev.isxander.evergreenhud.compatibility.fabric11701.impl

import dev.isxander.evergreenhud.compatibility.universal.impl.AILogger
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class LoggerImpl : AILogger() {
    private val logger: Logger = LogManager.getLogger("EvergreenHUD")

    override fun info(msg: String) = logger.info(msg)
    override fun warn(msg: String) = logger.warn(msg)
    override fun err(msg: String) = logger.error(msg)
}