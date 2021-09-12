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

import club.chachy.event.on
import dev.isxander.evergreenhud.api.forge10809.mc
import dev.isxander.evergreenhud.api.impl.UResolution
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.fml.common.gameevent.TickEvent

class ResolutionImpl : UResolution() {

    private var resolution: ScaledResolution = ScaledResolution(mc)

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