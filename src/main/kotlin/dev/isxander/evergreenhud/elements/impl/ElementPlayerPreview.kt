/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.elements.type.BackgroundElement
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.evergreenhud.utils.renderEntity
import dev.isxander.settxi.impl.int
import net.minecraft.client.util.math.MatrixStack

@ElementMeta(id = "evergreenhud:player_preview", name = "Player Preview", description = "Show a model of your player.", category = "Player")
class ElementPlayerPreview : BackgroundElement() {
    var rotation by int(0) {
        name = "Rotation"
        description = "The rotation of the player model."
        category = "Player Preview"
        range = 0..360
    }

    override val hitboxWidth = 80f
    override val hitboxHeight = 120f

    override fun render(matrices: MatrixStack, renderOrigin: RenderOrigin) {
        if (mc.player == null) return

        super.render(matrices, renderOrigin)

        val size = position.scale * 50
        val rotation = 360f - rotation

        mc.player!!.renderEntity(
            position.rawX + hitboxWidth / 2 * position.scale,
            position.rawY + hitboxHeight * position.scale - 15f * position.scale,
            size,
            rotation,
        )
    }
}
