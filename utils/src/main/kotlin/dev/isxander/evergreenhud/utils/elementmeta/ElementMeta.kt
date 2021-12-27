/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils.elementmeta

import kotlinx.serialization.Serializable

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Serializable(ElementMetaSerializer::class)
annotation class ElementMeta(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val credits: String = "",
    val maxInstances: Int = Int.MAX_VALUE
)
