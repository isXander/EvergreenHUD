package dev.isxander.evergreenhud.elements.abstractimp

import dev.isxander.evergreenhud.compatibility.universal.GL
import dev.isxander.evergreenhud.elements.Element
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.settings.SettingAdapter
import dev.isxander.evergreenhud.settings.impl.BooleanSetting
import dev.isxander.evergreenhud.settings.impl.ColorSetting
import dev.isxander.evergreenhud.settings.impl.FloatSetting
import dev.isxander.evergreenhud.utils.HitBox2D
import gg.essential.universal.UMatrixStack
import java.awt.Color

abstract class BackgroundElement : Element() {

    @BooleanSetting(name = "Enabled", category = ["Background"], description = "If the background is rendered.")
    val backgroundEnabled: SettingAdapter<Boolean> = SettingAdapter(true)
        .adaptSetter { enabled ->
            backgroundColor.set(if (enabled) Color(backgroundColor.get().red, backgroundColor.get().green, backgroundColor.get().blue, 100)
            else Color(backgroundColor.get().red, backgroundColor.get().green, backgroundColor.get().blue, 0))

            return@adaptSetter enabled
        }

    @ColorSetting(name = "Color", category = ["Background"], description = "The color of the background.")
    val backgroundColor: SettingAdapter<Color> = SettingAdapter(Color(0, 0, 0, 100))
        .adaptSetter {
            val enabled = it.alpha != 0
            if (backgroundEnabled.get() != enabled) backgroundEnabled.set(enabled)
            return@adaptSetter it
        }

    @BooleanSetting(name = "Enabled", category = ["Outline"], description = "If the background is rendered.")
    val outlineEnabled: SettingAdapter<Boolean> = SettingAdapter(false)
        .adaptSetter { enabled ->
            outlineColor.set(if (enabled) Color(outlineColor.get().red, outlineColor.get().green, outlineColor.get().blue, 255)
            else Color(outlineColor.get().red, outlineColor.get().green, outlineColor.get().blue, 0))

            return@adaptSetter enabled
        }

    @ColorSetting(name = "Color", category = ["Outline"], description = "The color of the outline.")
    val outlineColor: SettingAdapter<Color> = SettingAdapter(Color(0, 0, 0, 0))
        .adaptSetter {
            val enabled = it.alpha != 0
            if (outlineEnabled.get() != enabled) outlineEnabled.set(enabled)
            return@adaptSetter it
        }

    @FloatSetting(name = "Thickness", category = ["Outline"], description = "How thick the outline is.", min = 0.5f, 8f)
    var outlineThickness = 1f

    @FloatSetting(name = "Padding (Left)", category = ["Background", "Padding"], description = "How far the background extends to the left past the content.", min = 0f, max = 12f, suffix = " px")
    var paddingLeft = 4f
    @FloatSetting(name = "Padding (Right)", category = ["Background", "Padding"], description = "How far the background extends to the right past the content.", min = 0f, max = 12f, suffix = " px")
    var paddingRight = 4f
    @FloatSetting(name = "Padding (Top)", category = ["Background", "Padding"], description = "How far the background extends to the top past the content.", min = 0f, max = 12f, suffix = " px")
    var paddingTop = 4f
    @FloatSetting(name = "Padding (Bottom)", category = ["Background", "Padding"], description = "How far the background extends to the bottom past the content.", min = 0f, max = 12f, suffix = " px")
    var paddingBottom = 4f

    @FloatSetting(name = "Corner Radius", category = ["Background"], description = "How curvy the edges of the background is.", min = 0f, max = 6f)
    var cornerRadius = 0f

    override fun render(matrices: UMatrixStack, deltaTicks: Float, renderOrigin: RenderOrigin) {
        val bgCol = backgroundColor.get()
        val outlineCol = outlineColor.get()

        val scale = position.scale
        val hitbox = calculateHitBox(1f, scale)

        if (backgroundEnabled.get()) {
            GL.roundedRect(matrices, hitbox.x, hitbox.y, hitbox.width, hitbox.height, bgCol.rgb, cornerRadius)
        }
        if (outlineEnabled.get()) {
            GL.borderRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height, outlineCol.rgb, outlineThickness)
        }
    }

    override fun calculateHitBox(glScale: Float, drawScale: Float): HitBox2D {
        val width = hitboxWidth * drawScale
        val height = hitboxHeight * drawScale

        val top = paddingTop * drawScale
        val bottom = paddingBottom * drawScale
        val left = paddingLeft * drawScale
        val right = paddingRight * drawScale

        val x = position.rawX / glScale
        val y = position.rawY / glScale

        return HitBox2D(x - left, y - top, width + left + right, height + top + bottom)
    }

}
