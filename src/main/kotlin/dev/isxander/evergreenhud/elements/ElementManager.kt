/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.elements

import com.electronwill.nightconfig.core.Config
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.annotations.ElementMeta
import dev.isxander.evergreenhud.config.ElementConfig
import dev.isxander.evergreenhud.config.MainConfig
import dev.isxander.evergreenhud.event.EventListener
import dev.isxander.evergreenhud.gui.screens.ElementDisplay
import dev.isxander.evergreenhud.utils.jsonParser
import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.*
import dev.isxander.evergreenhud.utils.jsonFormat
import dev.isxander.evergreenhud.utils.logger
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.serialization.ConfigProcessor
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.util.math.MatrixStack
import java.io.InputStream
import kotlin.collections.ArrayList
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class ElementManager : ConfigProcessor, EventListener, Iterable<Element> {
    val availableElements: MutableMap<KClass<out Element>, ElementMeta> = mutableMapOf()
    val currentElements: ArrayList<Element> = ArrayList()

    /* Config */
    val mainConfig: MainConfig = MainConfig(this)
    val elementConfig: ElementConfig = ElementConfig(this)

    /* Settings */
    override val settings: MutableList<Setting<*>> = mutableListOf()

    var enabled by boolean(
        default = true,
        name = "Enabled",
        category = "General",
        description = "Display any elements you have created."
    )

    var checkForUpdates by boolean(
        default = true,
        name = "Check For Updates",
        category = "Connectivity",
        description = "Should EvergreenHUD check for updates when you start up the game."
    )

    var checkForSafety by boolean(
        default = true,
        name = "Check For Safety",
        category = "Connectivity",
        description = "Should EvergreenHUD check if the current version might have bannable features in them."
    )

    init {
        val coreElementSrc = this.javaClass.getResourceAsStream("/evergreenhud-elements.json")
        if (coreElementSrc == null) {
            logger.error("")
            logger.error("")
            logger.error("<----------------------------------------------->")
            logger.error("Core EvergreenHUD elements are unavailable!")
            logger.error("Please report this issue immediately!")
            logger.error("<----------------------------------------------->")
            logger.error("")
            logger.error("")
        } else {
            addSource(coreElementSrc, "core")
        }

        EvergreenHUD.eventBus.register(this)
    }

    fun addElement(element: Element) {
        currentElements.add(element)
        element.onAdded()
    }

    fun removeElement(element: Element) {
        currentElements.remove(element)
        element.onRemoved()
    }

    /**
     * Adds a JSON source to the list of available elements found in the jar
     */
    fun addSource(input: InputStream, name: String) {
        val elements = jsonParser.parse(input).get<List<Config>>("elements")

        var i = 0
        for (element in elements) {
            val meta = element.get<Config>("metadata")

            @Suppress("UNCHECKED_CAST")
            availableElements[Class.forName(element.get("class")).kotlin as KClass<out Element>] =
                ElementMeta(
                    meta["id"],
                    meta["name"],
                    meta["category"],
                    meta["description"],
                    meta["credits"],
                    meta["maxInstances"],
                )

            i++
        }

        logger.info("Registered $i elements from source: $name.")
    }

    fun getElementClass(id: String): KClass<out Element>? {
        for ((clazz, metadata) in availableElements) {
            if (metadata.id == id) return clazz
        }
        return null
    }

    /**
     * Create a new instance of an element
     * without the need to catch exceptions
     *
     * @param id the internal id of the element
     * @return element instance
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Element> getNewElementInstance(id: String): T? = getElementClass(id)?.createInstance() as T?

    inline fun <reified T : Element> getDummyElement(): T {
        return T::class.createInstance().apply {

        }
    }

    override fun onRenderHud(matrices: MatrixStack, tickDelta: Float) {
        mc.profiler.push("EvergreenHUD Render")

        val inChat = mc.currentScreen is ChatScreen
        val inDebug = mc.options.debugEnabled
        val inGui = mc.currentScreen != null && mc.currentScreen !is ElementDisplay && !inChat
        val inReplayViewer = mc.world != null && !mc.isInSingleplayer && mc.currentServerEntry == null && !mc.isConnectedToRealms

        for (e in currentElements) {
            if (
                (mc.mouse.isCursorLocked && !inDebug)
                || (e.showInChat && inChat)
                || (e.showInDebug && inDebug && !(!e.showInChat && inChat))
                || (e.showUnderGui && inGui)
                || (e.showInReplayViewer && inReplayViewer)
            ) {
                e.render(matrices, RenderOrigin.HUD)
            }
        }

        mc.profiler.pop()
    }

    var conf: Config
        get() {
            var data = Config.of(jsonFormat)

            for (setting in settings) {
                if (!setting.shouldSave) continue
                data = addSettingToConfig(setting, data)
            }

            return data
        }
        set(value) {
            for (setting in settings) {
                if (!setting.shouldSave) continue
                setSettingFromConfig(value, setting)
            }
        }

    override fun iterator(): Iterator<Element> = currentElements.iterator()

}
