package dev.isxander.evergreenhud.compatibility.forge10809.impl

import dev.isxander.evergreenhud.compatibility.forge10809.mc
import dev.isxander.evergreenhud.compatibility.universal.impl.AIMinecraft
import dev.isxander.evergreenhud.compatibility.universal.impl.entity.AIEntity
import net.minecraft.client.Minecraft
import java.io.File

class MinecraftImpl : AIMinecraft() {

    override val player: AIEntity get() = EntityImpl(mc.thePlayer)
    override val dataDir: File = mc.mcDataDir
    override val fps: Int get() = Minecraft.getDebugFPS()
    override val inGameHasFocus: Boolean get() = mc.currentScreen != null

}