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

package dev.isxander.evergreenhud.elements

import com.electronwill.nightconfig.core.Config
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.api.MCVersion
import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.*
import dev.isxander.evergreenhud.utils.*
import dev.isxander.settxi.serialization.ConfigProcessor
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

abstract class Element : ConfigProcessor {
    private var preloaded = false
    override val settings: MutableList<Setting<*>> = mutableListOf()
    val metadata: ElementMeta = this::class.findAnnotation()!!
    var position: Position2D =
        scaledPosition {
            x = 0.5f
            y = 0.5f
        }

    var scale by float(
        default = 100f,
        name = "Scale",
        description = "How large the element appears on the screen.",
        category = "Render",
        min = 50f,
        max = 500f,
        shouldSave = false,
    ) {
        set {
            position.scale = it / 100f
            it
        }
        get { position.scale * 100f }
    }

    var showInChat by boolean(
        default = false,
        name = "Show In Chat",
        category = "Visibility",
        description = "Render the element if you are in the chat. (Takes priority over show under gui)"
    )

    var showInDebug by boolean(
        default = false,
        name = "Show In F3",
        category = "Visibility",
        description = "Show the element if you have the debug screen (F3 menu) open.",
    )

    var showUnderGui by boolean(
        default = true,
        name = "Show Under GUIs",
        category = "Visibility",
        description = "Render the element even when you have a gui open."
    )

    /* called when element is added */
    open fun onAdded() {
        EvergreenHUD.eventBus.register(this)

    }
    /* called when element is removed */
    open fun onRemoved() {
        EvergreenHUD.eventBus.unregister(this)
        utilities.unregisterAllForObject(this)
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

    var conf: Config
        get() {
            val config = Config.of(tomlFormat).apply {
                set<Float>("x", position.scaledX)
                set<Float>("y", position.scaledY)
                set<Float>("scale", position.scale)
            }

            var settingsData = Config.of(tomlFormat)
            for (setting in settings) {
                if (!setting.shouldSave) continue
                settingsData = addSettingToConfig(setting, settingsData)
            }

            config.set<Config>("settings", settingsData)
            return config
        }
        set(value) {
            position.scaledX = value.get<Double>("x")?.toFloat() ?: position.scaledX
            position.scaledY = value.get<Double>("y")?.toFloat() ?: position.scaledY
            position.scale = value.get<Double>("scale")?.toFloat() ?: position.scale

            val settingsData = value["settings"] ?: Config.of(tomlFormat)
            for (setting in settings) {
                setSettingFromConfig(settingsData, setting)
            }
        }

    companion object {
        protected val utilities = ElementUtilitySharer()
    }

}

@Target(AnnotationTarget.CLASS)
annotation class ElementMeta(val id: String, val name: String, val category: String, val description: String, val allowedVersions: Array<MCVersion> = [MCVersion.FORGE_1_8_9, MCVersion.FABRIC_1_17_1], val maxInstances: Int = Int.MAX_VALUE)