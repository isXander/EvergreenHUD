/*
 * Copyright (C) isXander [2019 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud

import club.chachy.event.keventbus.on
import dev.isxander.evergreenhud.compatibility.universal.*
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.compatibility.universal.impl.keybind.CustomKeybind
import dev.isxander.evergreenhud.compatibility.universal.impl.keybind.Keyboard
import dev.isxander.evergreenhud.elements.impl.TestElement
import dev.isxander.evergreenhud.event.RenderHUDEvent
import dev.isxander.evergreenhud.event.TickEvent
import dev.isxander.evergreenhud.gui.MainGui
import me.kbrewster.eventbus.EventBus
import me.kbrewster.eventbus.Subscribe
import me.kbrewster.eventbus.eventbus
import me.kbrewster.eventbus.invokers.LMFInvoker
import java.awt.Color
import java.io.File

object EvergreenHUD {

    val DATA_DIR: File = File(MC.dataDir(), "evergreenhud")
    val EVENT_BUS: EventBus = eventbus {
        invoker { LMFInvoker() }
        threadSaftey { true }
        exceptionHandler { LOGGER.err("Error occurred in method: ${it.message}")  }
    }

    val elementManager: ElementManager = ElementManager()

    fun init() {
        EVENT_BUS.register(this)
        KEYBIND_MANAGER.registerKeybind(CustomKeybind(Keyboard.KEY_HOME, "Open EvergreenHUD GUI", "EvergreenHUD") { SCREEN_HANDLER.displayComponent(MainGui()) })

        LOGGER.info("\n\n\n\n\n\n${TestElement().init().generateJson().toPrettyString()}")
    }

    @Subscribe
    fun onRenderHud(event: RenderHUDEvent) {
        FONT_RENDERER.draw("FPS: ${MC.fps()}", 20f, 20f, Color(255, 50, 50).rgb, true)
    }

}