/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils

import net.minecraft.client.Mouse

val Mouse.scaledX: Double
    get() = x * mc.window.scaledWidth / mc.window.width

val Mouse.scaledY: Double
    get() = y * mc.window.scaledHeight / mc.window.height
