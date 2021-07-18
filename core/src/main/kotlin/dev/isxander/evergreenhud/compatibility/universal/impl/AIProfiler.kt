package dev.isxander.evergreenhud.compatibility.universal.impl

abstract class AIProfiler {

    abstract fun push(name: String)
    abstract fun pop()

}