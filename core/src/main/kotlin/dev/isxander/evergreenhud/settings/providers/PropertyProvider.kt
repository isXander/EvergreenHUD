/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.settings.providers

import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.isAccessible

class PropertyProvider<T>(private val obj: Any, private val property: KMutableProperty1<out Any, T>) : IValueProvider<T> {
    init {
        property.isAccessible = true
    }

    override var value: T
        get() = property.call(obj)
        set(value) { property.setter.call(obj, value) }
}