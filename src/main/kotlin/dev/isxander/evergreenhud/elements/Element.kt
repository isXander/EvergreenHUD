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
import dev.isxander.evergreenhud.event.EventListener
import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.*
import dev.isxander.evergreenhud.utils.*
import dev.isxander.settxi.serialization.ConfigProcessor
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack

abstract class Element : EventListener, Drawable, ConfigProcessor {
    private var preloaded = false
    override val settings: MutableList<Setting<*>> = mutableListOf()
    val metadata: Metadata = EvergreenHUD.elementManager.availableElements[this::class]!!
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
        description = "Render the element if you have the debug screen (F3 menu) open.",
    )

    var showUnderGui by boolean(
        default = true,
        name = "Show Under GUIs",
        category = "Visibility",
        description = "Render the element even when you have a gui open."
    )

    var showInReplayViewer by boolean(
        default = false,
        name = "Show In Replay Viewer",
        category = "Visibility",
        description = "Render the element if you are in the replay viewer."
    ) {
        depends { FabricLoader.getInstance().isModLoaded("replaymod") }
    }

    /* called when element is added */
    open fun onAdded() {
        eventBus.subscribe(this)

    }
    /* called when element is removed */
    open fun onRemoved() {
        eventBus.unsubscribe(this)
        utilities.unregisterAllForObject(this)
    }

    abstract fun render(matrices: MatrixStack, renderOrigin: RenderOrigin)

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
            val config = Config.of(jsonFormat)

            config.add("position", config.createSubConfig().apply {
                add("x", position.scaledX)
                add("y", position.scaledY)
                add("scale", position.scale)
                add("origin", position.origin.name)
            })

            var settingsData = config.createSubConfig()
            for (setting in settings) {
                if (!setting.shouldSave) continue
                settingsData = addSettingToConfig(setting, settingsData)
            }

            config.set<Config>("settings", settingsData)
            return config
        }
        set(value) {
            value.get<Config>("position").apply {
                position.scaledX = get<Number>("x")?.toFloat() ?: position.scaledX
                position.scaledY = get<Number>("y")?.toFloat() ?: position.scaledY
                position.scale = get<Number>("scale")?.toFloat() ?: position.scale
                position.origin = Position2D.Origin.valueOf(get("origin"))
            }

            val settingsData = value["settings"] ?: value.createSubConfig()
            for (setting in settings) {
                if (!setting.shouldSave) return
                setting.serializedValue = settingsData.getOrElse(setting.nameSerializedCategoryAndKey, setting.defaultSerializedValue)
            }
        }

    companion object {
        protected val utilities = ElementUtilitySharer()
        protected val eventBus by EvergreenHUD::eventBus
    }

    data class Metadata(
        val id: String,
        val name: String,
        val category: String,
        val description: String,
        val credits: String?,
        val maxInstances: Int,
    )
}

