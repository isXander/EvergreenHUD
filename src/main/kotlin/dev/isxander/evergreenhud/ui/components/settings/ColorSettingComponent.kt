/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui.components.settings

import dev.isxander.evergreenhud.settings.ColorSetting
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.chroma
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.effect
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.UMatrixStack

class ColorSettingComponent(val component: SettingComponent, val setting: ColorSetting) : UIComponent() {
    val outline = OutlineEffect(Color.black.awt, 1f, drawInsideChildren = true)

    init {
        constrain {
            width = AspectConstraint(1f)
        } effect outline

        onMouseClick {

        }

        onMouseEnter {
            outline::color.animate(Animations.IN_OUT_SIN, 0.3f, Color.white.awt)
        }
        onMouseLeave {
            outline::color.animate(Animations.IN_OUT_SIN, 0.3f, Color.black.awt)
        }
    }

    override fun draw(matrixStack: UMatrixStack) {
        beforeDrawCompat(matrixStack)

        val x = this.getLeft().toDouble()
        val y = this.getTop().toDouble()
        val x2 = this.getRight().toDouble()
        val y2 = this.getBottom().toDouble()

        val color = getColor()
        if (color.alpha == 0)
            return super.draw(matrixStack)

        if (setting.get().chroma.hasChroma)
            chroma(setting.get().chroma) {
                UIBlock.drawBlockWithActiveShader(matrixStack, Color.white.awt, x, y, x2, y2)
            }
        else
            UIBlock.drawBlock(matrixStack, color, x, y, x2, y2)

        super.draw(matrixStack)
    }
}
