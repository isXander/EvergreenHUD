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


@ElementMeta(
    id = "evergreenhud:player_preview",
    name = "Player Preview",
    description = "Show a model of your player.",
    category = "Player"
)
class ElementPlayerPreview : BackgroundElement() {
    var rotation by int(0) {
        name = "Rotation"
        description = "The rotation of the player model."
        category = "Player Preview"
        range = 0..360
    }

    override val hitboxWidth = 80f
    override val hitboxHeight = 120f

    override fun render(renderOrigin: RenderOrigin) {
        super.render(renderOrigin)
        mc.thePlayer.renderEntity(
            position.rawX + hitboxWidth / 2 * position.scale,
            position.rawY + hitboxHeight * position.scale - 15f * position.scale,
            position.scale,
            rotation
        )
    }
}
