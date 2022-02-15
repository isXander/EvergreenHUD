/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import dev.isxander.settxi.impl.DoubleSetting
import dev.isxander.settxi.impl.FloatSetting
import dev.isxander.settxi.impl.IntSetting
import dev.isxander.settxi.impl.LongSetting

var IntSetting.percentage: Float
    get() = getPercent(get().toFloat(), range.first.toFloat(), range.last.toFloat())
    set(value) = set(lerp(range.first.toFloat(), range.last.toFloat(), value).toInt())

var LongSetting.percentage: Float
    get() = getPercent(get().toFloat(), range.first.toFloat(), range.last.toFloat())
    set(value) = set(lerp(range.first.toFloat(), range.last.toFloat(), value).toLong())

var FloatSetting.percentage: Float
    get() = getPercent(get(), range.start, range.endInclusive)
    set(value) = set(lerp(range.start, range.endInclusive, value))

var DoubleSetting.percentage: Float
    get() = getPercent(get().toFloat(), range.start.toFloat(), range.endInclusive.toFloat())
    set(value) = set(lerp(range.start.toFloat(), range.endInclusive.toFloat(), value).toDouble())
