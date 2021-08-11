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

    @FloatSetting(name = "Scale", category = ["Render"], description = "How large the element is rendered.", min = 50f, max = 200f, suffix = "%", save = false)
    val scale = settingAdapter(100f) {
        set {
            position.scale = it / 100f
            return@set it
        }
    }

    @BooleanSetting(name = "Show In Chat", category = ["Visibility"], description = "Whether or not element should be displayed in the chat menu. (Takes priority over show under gui)")
    var showInChat: Boolean = false

    @BooleanSetting(name = "Show In F3", category = ["Visibility"], description = "Whether or not element should be displayed when you have the debug menu open.")
    var showInDebug: Boolean = false

    @BooleanSetting(name = "Show Under GUIs", category = ["Visibility"], description = "Whether or not element should be displayed when you have a gui open.")
    var showUnderGui: Boolean = false

    fun preload(): Element {
        if (preloaded) return this

        collectSettings(this) { settings.add(it) }
        init()

        preloaded = true
        return this
    }

    // called after settings have loaded
    open fun init() {}
    // called when element is added
    open fun onAdded() {
        EvergreenHUD.eventBus.register(this)
    }
    // called when element is removed
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

    override var conf: ConfigObject
        get() {
            var config = ConfigFactory.empty()
                .withValue("x", position.scaledX.asConfig())
                .withValue("y", position.scaledY.asConfig())
                .withValue("scale", position.scale.asConfig())
                .root()

            var settingsData = ConfigFactory.empty().root()
            for (setting in settings) {
                if (!setting.shouldSave) continue
                settingsData = addSettingToConfig(setting, settingsData)
            }
            config = config.withValue("settings", settingsData)

            return config
        }
        set(json) {
            position.scaledX = json.getOrDefault("x", position.scaledX.asConfig()).float()
            position.scaledY = json.getOrDefault("y", position.scaledY.asConfig()).float()
            position.scale = json.getOrDefault("scale", position.scale.asConfig()).float()

            val settingsData = json.getOrDefault("settings", ConfigFactory.empty().root()).obj()
            for (setting in settings) {
                var categoryData = settingsData
                for (categoryName in setting.category)
                    categoryData = categoryData.getOrDefault(categoryName, ConfigFactory.empty().root()).obj()

                setSettingFromConfig(categoryData, setting)
            }
        }

    companion object {
        val UTILITY_SHARER = ElementUtilitySharer()
    }

}

@Target(AnnotationTarget.CLASS)
annotation class ElementMeta(val id: String, val name: String, val category: String, val description: String, val allowedVersions: Array<MCVersion> = [MCVersion.FORGE_1_8_9, MCVersion.FABRIC_1_17_1], val maxInstances: Int = Int.MAX_VALUE)