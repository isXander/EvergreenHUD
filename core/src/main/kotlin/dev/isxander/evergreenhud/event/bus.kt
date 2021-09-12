/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.event

import club.chachy.event.api.bus.EventBus
import club.chachy.event.api.handler.Handler
import dev.isxander.evergreenhud.EvergreenHUD
import me.kbrewster.eventbus.Subscribe

inline fun <reified T : Any> on() = club.chachy.event.on<Any, T>(EvergreenBus())

class EvergreenBus : EventBus<Any> {
    override fun <T : Any> createHandler(event: Class<T>): Handler<T> = KEventBusHandler(event)
    override fun register(any: Any) = EvergreenHUD.eventBus.register(any)
}

class KEventBusHandler<T : Any>(private val eventClazz: Class<T>) : Handler<T>() {
    @Subscribe
    fun onEvent(event: T) {
        if (eventClazz == event::class.java) {
            process(event)
        }
    }
}