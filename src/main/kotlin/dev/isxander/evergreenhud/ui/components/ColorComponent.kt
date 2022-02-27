/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui.components

import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.chroma
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.universal.UMatrixStack

open class ColorComponent(var colorFunc: () -> Color) : UIComponent() {
    override fun draw(matrixStack: UMatrixStack) {
        beforeDrawCompat(matrixStack)

        val x = this.getLeft().toDouble()
        val y = this.getTop().toDouble()
        val x2 = this.getRight().toDouble()
        val y2 = this.getBottom().toDouble()

        val color = colorFunc()
        if (color.alpha == 0)
            return super.draw(matrixStack)

        if (color.chroma.hasChroma)
            chroma(color.chroma) {
                UIBlock.drawBlockWithActiveShader(matrixStack, color.awt, x, y, x2, y2)
            }
        else
            UIBlock.drawBlock(matrixStack, color.awt, x, y, x2, y2)

        super.draw(matrixStack)
    }
}
