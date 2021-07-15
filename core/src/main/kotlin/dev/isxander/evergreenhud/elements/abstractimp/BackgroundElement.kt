package dev.isxander.evergreenhud.elements.abstractimp

import dev.isxander.evergreenhud.elements.Element
import dev.isxander.evergreenhud.settings.SettingAdapter
import dev.isxander.evergreenhud.settings.impl.BooleanSetting
import dev.isxander.evergreenhud.settings.impl.ColorSetting
import java.awt.Color

abstract class BackgroundElement : Element() {

    @BooleanSetting(name = "Enabled", category = ["Color", "Background"], description = "If the background is rendered.")
    var backgroundEnabled = SettingAdapter(true)
        .modSet {
            return@modSet it
        }

    @ColorSetting(name = "Color", category = ["Color", "Background"], description = "The color of the background.")
    val backgroundColor = Color(0, 0, 0, 100)

    override fun render(partialTicks: Float, renderOrigin: Int) {
        TODO("Not yet implemented")
    }

}
