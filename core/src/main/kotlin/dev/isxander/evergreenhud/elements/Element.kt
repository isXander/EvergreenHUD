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

import com.uchuhimo.konf.Config
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.compatibility.universal.MCVersion
import dev.isxander.evergreenhud.config.ConfigProcessor
import dev.isxander.evergreenhud.settings.Setting
import dev.isxander.evergreenhud.settings.settingAdapter
import dev.isxander.evergreenhud.settings.impl.*
import dev.isxander.evergreenhud.utils.*
import kotlin.reflect.full.findAnnotation

abstract class Element : ConfigProcessor {

    private var preloaded = false
    val settings: ArrayList<Setting<*, *>> = ArrayList()
    val metadata: ElementMeta = this::class.findAnnotation()!!
    var position: Position2D =
        scaledPosition {
            x = 0.5f
            y = 0.5f
        }

    @FloatSetting(name = "Scale", category = "Render", description = "How large the element is rendered.", min = 50f, max = 200f, suffix = "%", save = false)
    val scale = settingAdapter(100f) {
        set {
            position.scale = it / 100f
            it
        }
        get { position.scale * 100f }
    }

    @BooleanSetting(name = "Show In Chat", category = "Visibility", description = "Whether or not element should be displayed in the chat menu. (Takes priority over show under gui)")
    var showInChat: Boolean = false

    @BooleanSetting(name = "Show In F3", category = "Visibility", description = "Whether or not element should be displayed when you have the debug menu open.")
    var showInDebug: Boolean = false

    @BooleanSetting(name = "Show Under GUIs", category = "Visibility", description = "Whether or not element should be displayed when you have a gui open.")
    var showUnderGui: Boolean = false

    fun preload(): Element {
        if (preloaded) return this

        preinit()
        collectSettings(this, settings::add)
        init()
        for (setting in settings) LOGGER.info(setting.name)

        preloaded = true
        return this
    }

    /* called before settings have loaded */
    open fun preinit() {}
    /* called after settings have loaded */
    open fun init() {}

    /* called when element is added */
    open fun onAdded() {
        EvergreenHUD.eventBus.register(this)
    }
    /* called when element is removed */
    open fun onRemoved() {
        EvergreenHUD.eventBus.unregister(this)
    }

    abstract fun render(deltaTicks: Float, renderOrigin: RenderOrigin)

    abstract fun calculateHitBox(glScale: Float, drawScale: Float): HitBox2D
    protected open val hitboxWidth = 10f
    protected open val hitboxHeight = 10f

    fun resetSettings(save: Boolean = false) {
        position = Position2D.scaledPositioning(0.5f, 0.5f, 1f)

        for (s in settings) s.reset()
        if (save) EvergreenHUD.elementManager.elementConfig.save()
    }

    override var conf: Config
        get() {
            val config = Config {
                this["x"] = position.scaledX
                this["y"] = position.scaledY
                this["scale"] = position.scale
            }

            var settingsData = Config()
            for (setting in settings) {
                if (!setting.shouldSave) continue
                settingsData = addSettingToConfig(setting, settingsData)
            }

            config["settings"] = settingsData
            return config
        }
        set(value) {
            position.scaledX = value.getOrNull("x") ?: position.scaledX
            position.scaledY = value.getOrNull("y") ?: position.scaledY
            position.scale = value.getOrNull("scale") ?: position.scale

            val settingsData = value.getOrNull("settings") ?: Config()
            for (setting in settings) {
                var category: Config = settingsData[setting.category]
                if (setting.subcategory != "") category = value[setting.subcategory]
                setSettingFromConfig(category, setting)
            }
        }

    companion object {
        val utilities = ElementUtilitySharer()
    }

}

@Target(AnnotationTarget.CLASS)
annotation class ElementMeta(val id: String, val name: String, val category: String, val description: String, val allowedVersions: Array<MCVersion> = [MCVersion.FORGE_1_8_9, MCVersion.FABRIC_1_17_1], val maxInstances: Int = Int.MAX_VALUE)