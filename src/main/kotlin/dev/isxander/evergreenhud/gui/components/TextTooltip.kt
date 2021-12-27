/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.gui.components

import dev.isxander.evergreenhud.utils.mc
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import java.util.function.Consumer
import kotlin.math.max

class TextTooltip(val screen: Screen, val description: Text, private val maxWidth: Int = max(screen.width / 2 - 43, 170)) : ButtonWidget.TooltipSupplier {
    override fun onTooltip(button: ButtonWidget, matrices: MatrixStack, mouseX: Int, mouseY: Int) {
        screen.renderOrderedTooltip(matrices, mc.textRenderer.wrapLines(description, maxWidth), mouseX, mouseY)
    }

    override fun supply(consumer: Consumer<Text>) = consumer.accept(description)
}
