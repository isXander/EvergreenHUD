package dev.isxander.evergreenhud.compatibility.fabric11701.impl

import dev.isxander.evergreenhud.compatibility.fabric11701.mc
import dev.isxander.evergreenhud.compatibility.universal.impl.AIProfiler

class ProfilerImpl : AIProfiler() {
    override fun push(name: String) = mc.profiler.push(name)
    override fun pop() = mc.profiler.pop()
}