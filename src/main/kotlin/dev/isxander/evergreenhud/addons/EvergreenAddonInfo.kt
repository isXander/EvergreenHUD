/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.addons

import java.util.jar.JarFile

data class EvergreenAddonInfo(val id: String, val name: String, val author: String, val entrypoints: List<AddonInitializer>, val jarFile: JarFile)
