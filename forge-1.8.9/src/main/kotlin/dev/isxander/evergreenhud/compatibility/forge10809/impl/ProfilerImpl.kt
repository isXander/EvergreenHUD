package dev.isxander.evergreenhud.compatibility.forge10809.impl

import dev.isxander.evergreenhud.compatibility.forge10809.mc
import dev.isxander.evergreenhud.compatibility.universal.impl.AIProfiler

class ProfilerImpl : AIProfiler() {
    override fun push(name: String) = mc.mcProfiler.startSection(name)
    override fun pop() = mc.mcProfiler.endSection()
}