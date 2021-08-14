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

package dev.isxander.evergreenhud.settings

fun <T> settingAdapter(value: T, lambda: SettingAdapter<T>.() -> Unit): SettingAdapter<T> {
    return SettingAdapter(value).apply(lambda)
}

class SettingAdapter<T> internal constructor(var value: T) {
    private var getter: (T) -> T = { it }
    private var setter: (T) -> T = { it }
    private var depends: MutableList<(T) -> Boolean> = mutableListOf()

    fun get(lambda: (T) -> T) { getter = lambda }
    fun set(lambda: (T) -> T) { setter = lambda }
    fun depends(lambda: (T) -> Boolean) = depends.add(lambda)

    fun get(): T = getter.invoke(value)
    fun set(new: T) {
        value = setter.invoke(new)
    }

    val hidden: Boolean
        get() = !depends.all { it(value) }
}