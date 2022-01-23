/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.event

import java.util.function.Consumer
import java.util.function.Function
import kotlin.reflect.KClass

class EventBus {
    val listeners = mutableMapOf<KClass<out Event>, MutableList<EventListener<out Event, *>>>()

    fun <T : Event, R : Any?> registerReturnable(event: KClass<T>, defaultCache: R? = null, predicate: (T) -> Boolean = { true }, executor: (T) -> R?): EventListener<T, R?> {
        val listener = EventListener<T, R?>(defaultCache, predicate, executor)

        if (listeners.containsKey(event)) {
            listeners[event]!!.add(listener)
        } else {
            listeners[event] = mutableListOf(listener)
        }

        return listener
    }

    inline fun <reified T : Event, R : Any?> registerReturnable(defaultCache: R? = null, noinline predicate: (T) -> Boolean = { true }, noinline executor: (T) -> R?): EventListener<T, R?> {
        return registerReturnable(T::class, defaultCache, predicate, executor)
    }

    inline fun <reified T : Event> register(noinline predicate: (T) -> Boolean = { true }, noinline executor: (T) -> Unit): EventListener<T, Unit?> {
        return registerReturnable(Unit, predicate, executor)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Event> post(event: T): T {
        if (listeners.containsKey(T::class)) {
            listeners[T::class]!!.forEach {
                (it as EventListener<T, *>).onEvent(event)
            }
        }

        return event
    }

    /* Java Users are annoying */

    fun <T : Event> post(type: Class<T>, event: T): T {
        val kotlinClass = type.kotlin
        if (listeners.containsKey(kotlinClass)) {
            listeners[kotlinClass]!!.forEach {
                (it as EventListener<T, *>).onEvent(event)
            }
        }

        return event
    }

    @JvmOverloads
    fun <T : Event, R : Any?> registerReturnable(event: Class<T>, defaultCache: R? = null, predicate: Function<T, Boolean> = Function { true }, executor: Function<T, R>): EventListener<T, R?> {
        return registerReturnable(event.kotlin, defaultCache, { predicate.apply(it) }) { executor.apply(it) }
    }

    @JvmOverloads
    fun <T : Event> register(event: Class<T>, predicate: Function<T, Boolean> = Function { true }, executor: Consumer<T>): EventListener<T, Unit?> {
        return registerReturnable(event.kotlin, Unit, { predicate.apply(it) }) { executor.accept(it) }
    }
}
