/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.elements.impl

import dev.deamsy.eventbus.api.listener.EventListener
import dev.isxander.evergreenhud.annotations.ElementMeta
import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.event.RenderTickEvent
import kotlin.math.roundToInt

@ElementMeta(id = "FPS", name = "FPS Display", category = "Simple", description = "Display how many times your screen is updating every second.")
class ElementFps : SimpleTextElement() {
    private var lastTime = System.currentTimeMillis().toDouble()
    private val frameTimes = ArrayList<Double>()

    init {
        title = "FPS"
        cacheTime = 20
    }

    override fun calculateValue(): String {
        // calculate mean of frame times and convert to FPS
        val fps = (1000 / frameTimes.average()).roundToInt().toString()
        frameTimes.clear()
        return fps
    }

    @EventListener
    fun onRender(event: RenderTickEvent) {
        frameTimes.add(System.currentTimeMillis() - lastTime)
        lastTime = System.currentTimeMillis().toDouble()
    }
}
