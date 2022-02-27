/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import gg.essential.api.utils.Multithreading
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.time.Duration

class CacheHolder<T>(val cacheDuration: Duration, val action: () -> T) : ReadOnlyProperty<Any?, T> {
    private var time = System.currentTimeMillis()

    var value: T = action()
        get() {
            if (System.currentTimeMillis() - time >= cacheDuration.inWholeMilliseconds) {
                field = action()
                time = System.currentTimeMillis()
            }

            return field
        }
        private set

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
}

//TODO: update to Duration Kotlin api when essential updates to kotlin 1.6
class AsyncCacheHolder<T>(private val cacheDuration: Long, val action: () -> T) : ReadOnlyProperty<Any?, T?> {
    private var time = System.currentTimeMillis()

    var value: T? = null
        get() {
            if (System.currentTimeMillis() - time >= cacheDuration) {
                Multithreading.runAsync {
                    field = action()
                    time = System.currentTimeMillis()
                }
            }

            return field
        }
        private set

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? = value
}
