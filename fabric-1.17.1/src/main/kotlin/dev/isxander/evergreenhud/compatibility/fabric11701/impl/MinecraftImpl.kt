package dev.isxander.evergreenhud.compatibility.fabric11701.impl

import dev.isxander.evergreenhud.compatibility.fabric11701.mc
import dev.isxander.evergreenhud.compatibility.fabric11701.mixins.AccessorMinecraftClient
import dev.isxander.evergreenhud.compatibility.universal.impl.AIMinecraft
import dev.isxander.evergreenhud.compatibility.universal.impl.entity.AIEntity
import java.io.File

class MinecraftImpl : AIMinecraft() {

    override val player: AIEntity get() = EntityImpl(mc.player)
    override val dataDir: File = mc.runDirectory
    override val fps: Int get() = AccessorMinecraftClient.getFps()
    override val inGameHasFocus: Boolean get() = mc.currentScreen != null

}