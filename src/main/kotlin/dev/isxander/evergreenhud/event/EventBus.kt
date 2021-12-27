/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.event

import io.ktor.util.collections.*
import java.util.function.Consumer

class EventBus<T : Any> {
    private val listeners = ConcurrentSet<T>()

    fun register(listener: T) {
        listeners.add(listener)
    }

    fun unregister(listener: T) {
        listeners.remove(listener)
    }

    fun post(executor: T.() -> Unit) {
        for (listener in listeners) listener.apply(executor)
    }

    fun postConsumer(consumer: Consumer<T>) {
        for (listener in listeners) consumer.accept(listener)
    }
}
