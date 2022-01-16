/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.event

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class EventListener<T, R : Any>(var cached: R? = null, private val predicate: (T) -> Boolean, private val executor: (T) -> R) : ReadWriteProperty<Any?, R> {
    fun onEvent(event: T) {
        if (predicate(event)) {
            cached = executor(event)
        }
    }

    fun get(): R = cached!!
    fun set(value: R) { cached = value }

    override fun getValue(thisRef: Any?, property: KProperty<*>): R = get()
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: R) = set(value)
}
