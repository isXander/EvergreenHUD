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

package dev.isxander.evergreenhud.elements

import club.chachy.event.keventbus.on
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.PROFILER
import dev.isxander.evergreenhud.config.ElementConfig
import dev.isxander.evergreenhud.config.MainConfig
import dev.isxander.evergreenhud.event.RenderHUDEvent
import dev.isxander.evergreenhud.settings.ConfigProcessor
import dev.isxander.evergreenhud.settings.Setting
import dev.isxander.evergreenhud.settings.impl.*
import dev.isxander.evergreenhud.utils.JsonObjectExt
import gg.essential.universal.UMatrixStack
import me.kbrewster.eventbus.Subscribe
import org.reflections.Reflections
import java.util.*
import kotlin.collections.ArrayList


class ElementManager : ConfigProcessor, Iterable<Element> {

    private val availableElements: MutableMap<String, Class<out Element>> = HashMap()
    val currentElements: ArrayList<Element> = ArrayList()

    /* Config */
    val mainConfig: MainConfig = MainConfig(this)
    val elementConfig: ElementConfig = ElementConfig(this)

    /* Settings */
    val settings: MutableList<Setting<*, *>> = ArrayList()

    @BooleanSetting(name = "Enabled", category = ["General"], description = "Display any elements you have created.")
    var enabled = true

    @BooleanSetting(name = "Check For Updates", category = ["Connectivity"], "Should EvergreenHUD check for updates when you start up the game.")
    var checkForUpdates = true

    @BooleanSetting(name = "Check For Safety", category = ["Connectivity"], "(HIGHLY RECOMMENDED) Should EvergreenHUD check if the current version of the mod you are playing on has been known to cause issues like an unfair advantage.")
    var checkForSafety = true

    init {
        collectSettings(this) { settings.add(it) }
        findAndRegisterElements()

        EvergreenHUD.EVENT_BUS.register(this)
    }

    @Suppress("UNCHECKED_CAST")
    private fun findAndRegisterElements() {
        val reflections = Reflections("")
        for (clazz in reflections.getTypesAnnotatedWith(ElementMeta::class.java)) {
            val annotation = clazz.getAnnotation(ElementMeta::class.java)
            availableElements[annotation.id] = clazz as Class<out Element>
        }
    }

    fun getAvailableElements(): Map<String, Class<out Element>> {
        return Collections.unmodifiableMap(availableElements)
    }

    /**
     * Create a new instance of an element
     * without the need to catch exceptions
     *
     * @param id the internal id of the element
     * @return element instance
     */
    fun getNewElementInstance(id: String?): Element? {
        val elementClass = availableElements[id] ?: return null
        return elementClass.newInstance()
    }

    /**
     * @param element get the identifier of an instance of an element
     */
    fun getElementId(element: Element): String? {
        for ((id, clazz) in availableElements) {
            if (clazz == element.javaClass) {
                return id
            }
        }
        return null
    }

    @Subscribe
    fun renderElements(event: RenderHUDEvent) {
        PROFILER.push("EvergreenHUD Render")
        for (e in currentElements) {
            e.render(event.matrices, event.dt, RenderOrigin.HUD)
        }
        PROFILER.pop()
//        mc.mcProfiler.startSection("Element Render")
//
//        val inChat = mc.currentScreen is GuiChat
//        val inDebug = mc.gameSettings.showDebugInfo
//        val inGui = mc.currentScreen != null && mc.currentScreen !is GuiScreenElements && mc.currentScreen !is GuiChat
//
//        for (e in currentElements) {
//            if (mc.inGameHasFocus && !inDebug || e.showInChat && inChat || e.showInDebug && inDebug && !(!e.showInChat && inChat) || e.showUnderGui && inGui) {
//                e.render(partialTicks, RenderOrigin.HUD)
//            }
//        }
//        mc.mcProfiler.endSection()
    }

    override var json: JsonObjectExt
        get() {
            val json = JsonObjectExt()

            for (setting in settings) {
                if (!setting.shouldSave) continue
                addSettingToJson(setting, json)
            }

            return json
        }
        set(value) {
            for (setting in settings) {
                var categoryJson = value
                for (categoryName in setting.category)
                    categoryJson = categoryJson[categoryName, JsonObjectExt()]!!

                setSettingFromJson(categoryJson, setting)
            }
        }

    override fun iterator(): Iterator<Element> = currentElements.iterator()

}