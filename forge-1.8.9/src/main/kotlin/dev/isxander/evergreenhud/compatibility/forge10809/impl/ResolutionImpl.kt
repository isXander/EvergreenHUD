package dev.isxander.evergreenhud.compatibility.forge10809.impl

import club.chachy.event.forge.on
import dev.isxander.evergreenhud.compatibility.forge10809.mc
import dev.isxander.evergreenhud.compatibility.universal.impl.AIResolution
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.fml.common.gameevent.TickEvent

class ResolutionImpl : AIResolution() {

    private lateinit var resolution: ScaledResolution

    init {
        on<TickEvent.RenderTickEvent>()
            .subscribe { resolution = ScaledResolution(mc) }
    }

    override val displayWidth: Int
        get() = mc.displayWidth
    override val displayHeight: Int
        get() = mc.displayHeight
    override val scaledWidth: Int
        get() = resolution.scaledWidth
    override val scaledHeight: Int
        get() = resolution.scaledHeight
    override val scaleFactor: Double
        get() = resolution.scaleFactor.toDouble()
}