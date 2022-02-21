/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.ui.components.settings

import dev.isxander.evergreenhud.settings.ColorSetting
import dev.isxander.evergreenhud.ui.EvergreenPalette
import dev.isxander.evergreenhud.ui.components.ColorComponent
import dev.isxander.evergreenhud.utils.AwtColor
import dev.isxander.evergreenhud.utils.Color
import dev.isxander.evergreenhud.utils.DrawMode
import dev.isxander.evergreenhud.utils.tessellate
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.GradientComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.constraints.AspectConstraint
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import net.minecraft.client.render.VertexFormats

class ColorSettingComponent(val component: SettingComponent, val setting: ColorSetting) : ColorComponent({ setting.get() }) {
    val outline = OutlineEffect(Color.black.awt, 1f, drawInsideChildren = true)

    var expanded = false
    val popout by ColorPopout().constrain {
        x = (-900).percent()
        y = 100.percent()
        width = 1000.percent()
        height = 600.percent()
    } effect OutlineEffect(Color.black.awt, 1f) childOf this

    init {
        popout.hide()

        constrain {
            width = AspectConstraint(1f)
        } effect outline

        onMouseClick {
            if (expanded) {
                expanded = false
                popout.hide()
                popout.setFloating(false)
            } else {
                expanded = true
                popout.unhide()
                popout.setFloating(true)
            }

            it.stopPropagation()
        }

        onMouseEnter {
            outline::color.animate(Animations.IN_OUT_SIN, 0.3f, Color.white.awt)
        }
        onMouseLeave {
            outline::color.animate(Animations.IN_OUT_SIN, 0.3f, Color.black.awt)
        }
    }

    inner class ColorPopout : UIBlock(EvergreenPalette.Greyscale.Dark2.awt) {
        var currentHue: Float
        var currentSaturation: Float
        var currentBrightness: Float

        private val hueColorList: List<Color> = (0..50).map { i -> Color(AwtColor.HSBtoRGB(i / 50f, 1f, 0.7f), false) }

        init {
            val color = setting.get()
            val hsb = AwtColor.RGBtoHSB(color.red, color.green, color.blue, null)

            currentHue = hsb[0]
            currentSaturation = hsb[1]
            currentBrightness = hsb[2]
        }

        val paddedContent by UIContainer().constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 90.percent()
            height = 80.percent()
        } childOf this

        val hueSaturationBox by UIContainer().constrain {
            y = CenterConstraint()
            width = 45.percent()
            height = 100.percent()
        }.onMouseClick {
            currentSaturation = it.relativeX / getWidth()
            currentBrightness = 1 - (it.relativeY / getHeight())

            updateColor()

            it.stopImmediatePropagation()
        }.onMouseDrag { mouseX, mouseY, _ ->
            currentSaturation = (mouseX / getWidth()).coerceIn(0f..1f)
            currentBrightness = 1 - (mouseY / getHeight()).coerceIn(0f..1f)

            updateColor()
        } effect OutlineEffect(Color.white.awt, 1f) childOf paddedContent

        val saturationGradient by GradientComponent(endColor = getHueRGB().awt, direction = GradientComponent.GradientDirection.LEFT_TO_RIGHT).constrain {
            width = 100.percent()
            height = 100.percent()
        } childOf hueSaturationBox

        val brightnessGradient by GradientComponent(startColor = Color.black.awt, endColor = Color.none.awt, direction = GradientComponent.GradientDirection.BOTTOM_TO_TOP).constrain {
            width = 100.percent()
            height = 100.percent()
        } childOf hueSaturationBox

        val selector by ColorComponent { setting.get() }.constrain {
            x = basicXConstraint { RelativeConstraint(currentSaturation).getXPositionImpl(it) - 2.5.percent().getHeightImpl(it) }
            y = basicYConstraint { RelativeConstraint(1 - currentBrightness).getYPositionImpl(it) } - 2.5.percent()
            width = AspectConstraint()
            height = 5.percent()
        } effect OutlineEffect(Color.black.awt, 1f) childOf hueSaturationBox

        init {
            paddedContent.addChild(object : UIComponent() {
                override fun draw(matrixStack: UMatrixStack) {
                    drawHueLine(matrixStack)
                    super.draw(matrixStack)
                }
            }.constrain {
                x = 50.percent()
                width = 10.percent()
                height = 80.percent()
            })
        }

        private fun updateColor() {
            setting.set(Color(AwtColor.HSBtoRGB(currentHue, currentSaturation, currentBrightness), false, setting.get().chroma).withAlpha(setting.get().alpha))
        }

        private fun getHueRGB(): Color {
            return Color(AwtColor.HSBtoRGB(currentHue, 1f, 1f), false)
        }

        private fun UIComponent.drawHueLine(matrices: UMatrixStack) {
            val left = getLeft().toDouble()
            val top = getTop().toDouble()
            val right = getRight().toDouble()
            val height = getHeight().toDouble()

            UGraphics.enableBlend()
            UGraphics.disableAlpha()
            UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)
            UGraphics.shadeModel(7425)

            matrices.tessellate(DrawMode.QUADS, VertexFormats.POSITION_COLOR) {
                var first = true
                for ((i, color) in hueColorList.withIndex()) {
                    val yPos = top + (i.toFloat() * height / 50.0)
                    if (!first) {
                        vertex {
                            pos(left, yPos, 0.0)
                            color(color)
                        }
                        vertex {
                            pos(right, yPos, 0.0)
                            color(color)
                        }
                    }

                    vertex {
                        pos(right, yPos, 0.0)
                        color(color)
                    }
                    vertex {
                        pos(left, yPos, 0.0)
                        color(color)
                    }

                    first = false
                }
            }

            UGraphics.shadeModel(7424)
            UGraphics.disableBlend()
            UGraphics.enableAlpha()
        }
    }
}
