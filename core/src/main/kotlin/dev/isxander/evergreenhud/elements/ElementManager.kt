/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.elements

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigObject
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.MC_VERSION
import dev.isxander.evergreenhud.compatibility.universal.PROFILER
import dev.isxander.evergreenhud.config.ConfigProcessor
import dev.isxander.evergreenhud.config.ElementConfig
import dev.isxander.evergreenhud.config.MainConfig
import dev.isxander.evergreenhud.event.RenderHUDEvent
import dev.isxander.evergreenhud.event.on
import dev.isxander.evergreenhud.settings.Setting
import dev.isxander.evergreenhud.settings.impl.*
import dev.isxander.evergreenhud.utils.obj
import me.kbrewster.eventbus.Subscribe
import org.reflections.Reflections
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class ElementManager : ConfigProcessor, Iterable<Element> {

    private val availableElements: MutableMap<String, KClass<out Element>> = HashMap()
    private val currentElements: ArrayList<Element> = ArrayList()

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

        on<RenderHUDEvent>()
            .filter { enabled }
            .subscribe {  }
        EvergreenHUD.eventBus.register(this)
    }

    fun addElement(element: Element) {
        if (!element.metadata.allowedVersions.contains(MC_VERSION)) throw IllegalArgumentException("Element not compatible with this version.")

        currentElements.add(element)
        element.onAdded()
    }

    fun removeElement(element: Element) {
        currentElements.remove(element)
        element.onRemoved()
    }

    @Suppress("UNCHECKED_CAST")
    private fun findAndRegisterElements() {
        val reflections = Reflections("")
        for (clazz in reflections.getTypesAnnotatedWith(ElementMeta::class.java)) {
            val annotation = clazz.getAnnotation(ElementMeta::class.java)
            availableElements[annotation.id] = clazz.kotlin as KClass<out Element>
        }
    }

    fun getAvailableElements(): Map<String, KClass<out Element>> {
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
        return availableElements[id]?.createInstance()
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

    fun getCurrentElements(): List<Element> {
        return Collections.unmodifiableList(currentElements)
    }

    fun clearElements() {
        currentElements.clear()
    }

    @Subscribe
    fun renderElements(event: RenderHUDEvent) {
        PROFILER.push("EvergreenHUD Render")
        for (e in currentElements) {
            e.render(event.dt, RenderOrigin.HUD)
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

    override var conf: ConfigObject
        get() {
            var data = ConfigFactory.empty().root()

            for (setting in settings) {
                if (!setting.shouldSave) continue
                data = addSettingToConfig(setting, data)
            }

            return data
        }
        set(value) {
            for (setting in settings) {
                var categoryData = value
                for (categoryName in setting.category)
                    categoryData = categoryData.getOrDefault(categoryName, ConfigFactory.empty().root()).obj()

                setSettingFromConfig(categoryData, setting)
            }
        }

    override fun iterator(): Iterator<Element> = currentElements.iterator()

}