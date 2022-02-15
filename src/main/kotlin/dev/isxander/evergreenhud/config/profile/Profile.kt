/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.config.profile

import dev.isxander.evergreenhud.utils.base64
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    val name: String,
    val description: String,
    val icon: String = this::class.java.getResourceAsStream("/assets/evergreenhud/evergreenhud.png")!!.base64 //todo check if this actually works with the forge move resources hack
)
