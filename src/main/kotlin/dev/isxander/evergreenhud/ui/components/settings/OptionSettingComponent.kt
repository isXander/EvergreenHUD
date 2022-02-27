/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui.components.settings

import dev.isxander.evergreenhud.ui.EvergreenPalette
import dev.isxander.evergreenhud.ui.effects.RotateEffect
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.ofIdentifier
import dev.isxander.evergreenhud.utils.resource
import dev.isxander.settxi.impl.OptionSetting
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.UMatrixStack

class OptionSettingComponent(val component: SettingComponent, val setting: OptionSetting) : UIBlock(EvergreenPalette.Greyscale.Dark2.awt) {
    var expanded = false
        private set

    init {
        constrain {
            width = AspectConstraint(10.4f)
        } effect OutlineEffect(Color.black.awt, 1f, drawInsideChildren = true)

        onMouseClick {
            if (expanded) minimize() else expand()
        }
    }

    val current by UIText(setting.get().name, shadow = false).constrain {
        x = 2.percent()
        y = CenterConstraint()
        width = TextAspectConstraint()
        height = 75.percent()
    } childOf this

    val caretRotateEffect = RotateEffect(0f)
    val caret by UIImage.ofIdentifier(resource("ui/caret.png")).constrain {
        x = 0.pixels(alignOpposite = true) - 2.percent()
        y = CenterConstraint()
        width = ImageAspectConstraint()
        height = 75.percent()
    } effect caretRotateEffect childOf this

    val scroller by ScrollComponent().constrain {
        y = 100.percent()
        width = 100.percent()
        height = 400.percent()
    }.animateBeforeHide {
        setHeightAnimation(Animations.IN_OUT_SIN, 0.3f, 0.percent())
    }.animateAfterUnhide {
        setHeightAnimation(Animations.IN_OUT_SIN, 0.3f, 400.percent())
    } childOf this

    init {
        scroller.hide(true)

        for (option in setting.options) {
            val optionContainer by UIBlock(EvergreenPalette.Greyscale.Dark2.awt).constrain {
                y = SiblingConstraint()
                width = 100.percent()
                height = 25.percent()
            }.onMouseClick {
                minimize()
                setting.set(option)
                it.stopImmediatePropagation()
            } effect OutlineEffect(Color.black.awt, 1f, drawInsideChildren = true) childOf scroller

            val text by UIText(option.name, shadow = false).constrain {
                x = 2.percent()
                y = CenterConstraint()
                width = TextAspectConstraint()
                height = 75.percent()
            } childOf optionContainer
        }
    }

    fun expand() {
        if (expanded) return
        expanded = true

        caretRotateEffect::angle.animate(Animations.IN_OUT_CIRCULAR, 0.2f, 180f)
        scroller.unhide()
        setFloating(true)
    }

    fun minimize() {
        if (!expanded) return
        expanded = false

        caretRotateEffect::angle.animate(Animations.IN_OUT_CIRCULAR, 0.2f, 0f)
        scroller.hide()
    }

    override fun draw(matrixStack: UMatrixStack) {
        if (caretRotateEffect.angle == 0f) {
            setFloating(false)
        }
        super.draw(matrixStack)
    }
}


