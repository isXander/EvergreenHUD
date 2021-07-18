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

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.compatibility.universal.MCVersion
import dev.isxander.evergreenhud.settings.ConfigProcessor
import dev.isxander.evergreenhud.settings.JsonType
import dev.isxander.evergreenhud.settings.Setting
import dev.isxander.evergreenhud.settings.SettingAdapter
import dev.isxander.evergreenhud.settings.impl.*
import dev.isxander.evergreenhud.utils.HitBox2D
import dev.isxander.evergreenhud.utils.JsonObjectExt
import dev.isxander.evergreenhud.utils.Position
import gg.essential.universal.UMatrixStack

abstract class Element : ConfigProcessor {

    var preloaded = false
        private set
    val settings: ArrayList<Setting<*, *>> = ArrayList()
    val metadata: ElementMeta = this::class.java.getAnnotation(ElementMeta::class.java)
    var position: Position = Position.scaledPositioning(0.5f, 0.5f, 1f)
        private set

    @FloatSetting(name = "Scale", category = ["Render"], description = "How large the element is rendered.", min = 50f, max = 200f, suffix = "%", save = false)
    val scale = SettingAdapter(100f)
        .adaptSetter {
            position.scale = it / 100f
            return@adaptSetter it
        }

    @BooleanSetting(name = "Show In Chat", category = ["Visibility"], description = "Whether or not element should be displayed in the chat menu. (Takes priority over show under gui)")
    var showInChat: Boolean = false
    @BooleanSetting(name = "Show In F3", category = ["Visibility"], description = "Whether or not element should be displayed when you have the debug menu open.")
    var showInDebug: Boolean = false
    @BooleanSetting(name = "Show Under GUIs", category = ["Visibility"], description = "Whether or not element should be displayed when you have a gui open.")
    var showUnderGui: Boolean = false

    fun preload(): Element {
        if (preloaded) return this

        LOGGER.info("--------- PRELOADING ----------")
        collectSettings(this) {
            LOGGER.info("COLLECTED SETTING")
            settings.add(it)
        }

        preloaded = true
        return this
    }

    // called after settings have loaded
    open fun init() {}
    // called when element is added
    open fun onAdded() {}
    // called when element is removed
    open fun onRemoved() {}

    abstract fun render(matrices: UMatrixStack, deltaTicks: Float, renderOrigin: RenderOrigin)

    abstract fun calculateHitBox(glScale: Float, drawScale: Float): HitBox2D
    protected open val hitboxWidth = 10f
    protected open val hitboxHeight = 10f

    fun resetSettings(save: Boolean = false) {
        position = Position.scaledPositioning(0.5f, 0.5f, 1f)

        for (s in settings) s.reset()
        if (save) EvergreenHUD.elementManager.elementConfig.save()
    }

    override var json: JsonObjectExt
        get() {
            val json = JsonObjectExt()

            json["x"] = position.scaledX
            json["y"] = position.scaledY
            json["scale"] = position.scale
            val dynamic = JsonObjectExt()
            for (setting in settings) {
                if (!setting.shouldSave) continue
                addSettingToJson(setting, dynamic)
            }
            json["dynamic"] = dynamic

            return json
        }
        set(json) {
            position.scaledX = json["x", position.scaledX]
            position.scaledY = json["y", position.scaledY]
            position.scale = json["scale", position.scale]

            val dynamic = json["dynamic", JsonObjectExt()]!!
            for (setting in settings) {
                var categoryJson = dynamic
                for (categoryName in setting.category)
                    categoryJson = categoryJson[categoryName, JsonObjectExt()]!!

                setSettingFromJson(categoryJson, setting)
            }
        }

}

@Target(AnnotationTarget.CLASS)
annotation class ElementMeta(val id: String, val name: String, val category: String, val description: String, val allowedVersions: Array<MCVersion> = [MCVersion.FORGE_1_8_9, MCVersion.FABRIC_1_17_1], val maxInstances: Int = Int.MAX_VALUE)