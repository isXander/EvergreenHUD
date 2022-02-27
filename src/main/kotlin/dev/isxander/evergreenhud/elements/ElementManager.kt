/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.config.element.ElementConfig
import dev.isxander.evergreenhud.config.global.GlobalConfig
import dev.isxander.evergreenhud.event.ClientDisconnectEvent
import dev.isxander.evergreenhud.event.ClientJoinWorldEvent
import dev.isxander.evergreenhud.event.RenderHudEvent
import dev.isxander.evergreenhud.utils.decode
import dev.isxander.evergreenhud.utils.elementmeta.ElementListJson
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.json
import dev.isxander.evergreenhud.utils.logger
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.serialization.ConfigProcessor
import kotlinx.serialization.decodeFromString
import java.io.InputStream
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance

class ElementManager : ConfigProcessor, Iterable<Element> {
    val availableElements: MutableMap<KClass<out Element>, ElementMeta> = mutableMapOf()
    val currentElements: ArrayList<Element> = ArrayList()

    val blacklistedElements = mutableMapOf<String, List<String>>()

    /* Config */
    val globalConfig: GlobalConfig = GlobalConfig(this)
    val elementConfig: ElementConfig = ElementConfig(this)

    /* Settings */
    override val settings: MutableList<Setting<*>> = mutableListOf()

    var enabled by boolean(true) {
        name = "Enabled"
        category = "General"
        description = "Display any elements you have created."
    }

    var elementSnapping by boolean(true) {
        name = "Element Snapping"
        category = "General"
        description = "When dragging elements, EGH snaps it "
    }

    var checkForUpdates by boolean(true) {
        name = "Check For Updates"
        category = "Connectivity"
        description = "Should EvergreenHUD check for updates when you start up the game."
    }

    var checkForSafety by boolean(true) {
        name = "Check For Safety"
        category = "Connectivity"
        description = "Should EvergreenHUD check if the current version might have bannable features in them."
    }

    init {
        val coreElementSrc = this.javaClass.getResourceAsStream("/evergreenhud-elements.json")
        if (coreElementSrc == null) {
            logger.error("")
            logger.error("")
            logger.error("<----------------------------------------------->")
            logger.error("   Core EvergreenHUD elements are unavailable!   ")
            logger.error("      Please report this issue immediately!      ")
            logger.error("<----------------------------------------------->")
            logger.error("")
            logger.error("")
        } else {
            addSource(coreElementSrc, "core")
        }
    }

    fun isIdBlacklisted(id: String) =
        id in (blacklistedElements[mc.currentServerData?.serverIP] ?: emptyList())

    fun addElement(element: Element) {
        if (isIdBlacklisted(element.metadata.id))
            error("Cannot add blacklisted element defined by server!")

        currentElements.add(element)
        if (inGame) element.onAdded()
    }

    fun removeElement(element: Element) {
        currentElements.remove(element)
        element.onRemoved()
    }

    fun addSource(string: String, name: String) {
        val elements = json.decodeFromString<List<ElementListJson>>(string)

        var i = 0
        for (element in elements) {
            val meta = element.metadata
            @Suppress("UNCHECKED_CAST")
            val elementClass = Class.forName(element.`class`).kotlin as KClass<out Element>

            // TODO: make this not a runtime check if possible
            if (elementClass.constructors.none { it.parameters.all(KParameter::isOptional) }) {
                logger.warn("$name: ${elementClass.qualifiedName!!} does not have a constructor with no arguments. Cannot register!")
                continue
            }

            if (availableElements.containsKey(elementClass)) {
                val existing = availableElements[elementClass]!!
                logger.warn("Identical element ID ${existing.id} was attempted to be registered on classes ${elementClass.qualifiedName} and ${getElementClass(existing.id)?.qualifiedName}. Cannot register!")
                continue
            }

            availableElements[elementClass] =
                ElementMeta(
                    meta.decode<String>("id")!!,
                    meta.decode<String>("name")!!,
                    meta.decode<String>("category")!!,
                    meta.decode<String>("description")!!,
                    meta.decode<String>("credits")!!,
                    meta.decode<Int>("maxInstances")!!,
                )

            i++
        }

        logger.info("Registered $i elements from source: $name.")
    }

    /**
     * Adds a JSON source to the list of available elements found in the jar
     */
    fun addSource(input: InputStream, name: String) {
        addSource(input.readBytes().decodeToString(), name)
    }

    fun getElementClass(id: String): KClass<out Element>? {
        return availableElements.entries.find { (_, metadata) -> metadata.id == id }?.key
    }

    /**
     * Create a new instance of an element
     * without the need to catch exceptions
     *
     * @param id the internal id of the element
     * @return element instance
     */
    inline fun <reified T : Element> getNewElementInstance(id: String): T? = getElementClass(id)?.createInstance() as? T

    val renderHudEvent by EvergreenHUD.eventBus.register<RenderHudEvent>({ enabled }) {
        mc.mcProfiler.startSection("EvergreenHUD Render")

        for (e in currentElements) {
            if (e.canRenderInHUD()) {
                e.render(RenderOrigin.HUD)
            }
        }

        mc.mcProfiler.endSection()
    }

    val clientJoinWorldEvent by EvergreenHUD.eventBus.register<ClientJoinWorldEvent> {
        currentElements.forEach(Element::onAdded)
    }

    val clientDisconnectEvent by EvergreenHUD.eventBus.register<ClientDisconnectEvent> {
        currentElements.forEach(Element::onRemoved)
    }

    private val inGame: Boolean
        get() = mc.theWorld != null

    override fun iterator(): Iterator<Element> = currentElements.iterator()
}
