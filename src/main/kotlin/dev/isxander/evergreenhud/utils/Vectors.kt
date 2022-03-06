/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import net.minecraft.util.math.Vec3f
import kotlin.math.abs

operator fun Vec3f.times(other: Vec3f) =
    Vec3f(x * other.x, y * other.y, z * other.z)

operator fun Vec3f.times(scalar: Float) =
    Vec3f(x * scalar, y * scalar, z * scalar)

operator fun Float.times(value: Vec3f) =
    Vec3f(this * value.x, this * value.y, this * value.z)

operator fun Vec3f.div(other: Vec3f) =
    Vec3f(x / other.x, y / other.y, z / other.z)

operator fun Vec3f.div(value: Float) =
    Vec3f(x / value, y / value, z / value)

operator fun Vec3f.plus(other: Vec3f) =
    Vec3f(x + other.x, y + other.y, z + other.z)

operator fun Vec3f.plus(scalar: Float) =
    Vec3f(x + scalar, y + scalar, z + scalar)

operator fun Float.plus(other: Vec3f) =
    Vec3f(this + other.x, this + other.y, this + other.z)

operator fun Vec3f.minus(other: Vec3f) =
    Vec3f(x - other.x, y - other.y, z - other.z)

operator fun Vec3f.minus(scalar: Float) =
    Vec3f(x - scalar, y - scalar, z - scalar)

operator fun Float.minus(other: Vec3f) =
    Vec3f(this - other.x, this - other.y, this - other.z)

operator fun Vec3f.rem(scalar: Float) =
    Vec3f(x % scalar, y % scalar, z % scalar)

fun abs(other: Vec3f) =
    Vec3f(abs(other.x), abs(other.y), abs(other.z))

fun Vec3f.coerceIn(range: ClosedFloatingPointRange<Float>) =
    Vec3f(x.coerceIn(range), y.coerceIn(range), z.coerceIn(range))

fun lerp(a: Vec3f, b: Vec3f, t: Float) =
    Vec3f(lerp(a.x, b.x, t), lerp(a.y, b.y, t), lerp(a.z, b.z, t))

fun Vec3f(scalar: Float) =
    Vec3f(scalar, scalar, scalar)
