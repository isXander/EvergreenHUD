/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import net.minecraft.client.Mouse

val Mouse.scaledX: Double
    get() = x * mc.window.scaledWidth / mc.window.width

val Mouse.scaledY: Double
    get() = y * mc.window.scaledHeight / mc.window.height
