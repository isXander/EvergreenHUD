/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

interface Memoize {
    fun clear()
}

class Memoize1<T, R>(private val f: (T) -> R) : (T) -> R, Memoize {
    private val cache = mutableMapOf<T, R>()

    override operator fun invoke(t: T): R {
        val time = System.nanoTime()
        return f(t).also { println("Memoized in ${(System.nanoTime() - time) / 1e6}ms") }
    }

    override fun clear() = cache.clear()
}

class Memoize2<T1, T2, R>(private val f: (T1, T2) -> R) : (T1, T2) -> R, Memoize {
    private val cache = mutableMapOf<Pair<T1, T2>, R>()

    override operator fun invoke(t1: T1, t2: T2): R {
        return cache.getOrPut(Pair(t1, t2)) { f(t1, t2) }
    }

    override fun clear() = cache.clear()
}

fun <T, R> ((T) -> R).memoize() = Memoize1(this)
fun <T1, T2, R> ((T1, T2) -> R).memoize() = Memoize2(this)
