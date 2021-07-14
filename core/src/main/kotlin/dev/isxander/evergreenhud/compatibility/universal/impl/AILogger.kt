package dev.isxander.evergreenhud.compatibility.universal.impl

abstract class AILogger {

    abstract fun info(msg: String)
    abstract fun warn(msg: String)
    abstract fun err(msg: String)

}