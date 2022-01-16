/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.gui.screens.effects

import gg.essential.elementa.effects.Effect
import gg.essential.universal.UMatrixStack

class RotationEffect(
    private val degrees: Float
) : Effect() {
    override fun beforeDraw(matrices: UMatrixStack) {
        matrices.push()

        val x = (boundComponent.getLeft() + (boundComponent.getRight() - boundComponent.getLeft()) / 2).toDouble()
        val y = (boundComponent.getTop() + (boundComponent.getBottom() - boundComponent.getTop()) / 2).toDouble()

        matrices.translate(x, y, 0.0)
        matrices.rotate(degrees, 0f, 0f, 1f)
        matrices.translate(-x, -y, 0.0)
    }

    override fun afterDraw(matrices: UMatrixStack) {
        matrices.pop()
    }
}
