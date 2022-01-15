/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
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
    val maxInstances: Int = Int.MAX_VALUE,
)
