/*
 *
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
 *
 */

package dev.isxander.evergreenhud.settings

import dev.isxander.settxi.Setting
import dev.isxander.settxi.SettingAdapter
import dev.isxander.settxi.serialization.ConfigProcessor
import java.io.File

class FileSetting internal constructor(
    default: File,
    override val name: String,
    override val category: String,
    override val subcategory: String? = null,
    override val description: String,
    override val shouldSave: Boolean = true,
    lambda: SettingAdapter<File>.() -> Unit = {},
) : Setting<File>(default, lambda) {
    override var serializedValue: Any
        get() = value.path
        set(new) { value = File(new as String) }

    override val defaultSerializedValue: String = default.path
}

fun ConfigProcessor.file(
    default: File,
    name: String,
    category: String,
    subcategory: String? = null,
    description: String,
    shouldSave: Boolean = true,
    lambda: SettingAdapter<File>.() -> Unit = {},
): FileSetting {
    val setting = FileSetting(default, name, category, subcategory, description, shouldSave, lambda)
    this.settings.add(setting)
    return setting
}
