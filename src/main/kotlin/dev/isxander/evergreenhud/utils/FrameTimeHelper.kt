/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.event.RenderTickEvent
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FrameTimeHelper(eventPredicate: () -> Boolean) : ReadOnlyProperty<Any?, MutableList<Double>> {
    private var lastTime = System.currentTimeMillis().toDouble()
    val frameTimes = mutableListOf<Double>()

    val renderTickEvent by EvergreenHUD.eventBus.register<RenderTickEvent>({ eventPredicate() }) {
        frameTimes += System.currentTimeMillis() - lastTime
        lastTime = System.currentTimeMillis().toDouble()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>) = frameTimes
}
