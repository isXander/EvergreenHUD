package cc.polyfrost.evergreenhud.utils

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

class Memoize3<T1, T2, T3, R>(private val f: (T1, T2, T3) -> R) : (T1, T2, T3) -> R, Memoize {
    private val cache = mutableMapOf<Triple<T1, T2, T3>, R>()

    override operator fun invoke(t1: T1, t2: T2, t3: T3): R {
        return cache.getOrPut(Triple(t1, t2, t3)) { f(t1, t2, t3) }
    }

    override fun clear() = cache.clear()
}

fun <T, R> ((T) -> R).memoize() = Memoize1(this)
fun <T1, T2, R> ((T1, T2) -> R).memoize() = Memoize2(this)
fun <T1, T2, T3, R> ((T1, T2, T3) -> R).memoize() = Memoize3(this)