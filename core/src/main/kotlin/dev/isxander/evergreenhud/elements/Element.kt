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

import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.compatibility.universal.MCVersion
import dev.isxander.evergreenhud.settings.ConfigProcessor
import dev.isxander.evergreenhud.settings.JsonValues
import dev.isxander.evergreenhud.settings.Setting
import dev.isxander.evergreenhud.settings.SettingAdapter
import dev.isxander.evergreenhud.settings.impl.*
import dev.isxander.evergreenhud.utils.JsonObjectExt
import dev.isxander.evergreenhud.utils.Position

abstract class Element : ConfigProcessor {

    var preloaded = false
        private set
    val settings: MutableList<Setting<*, *>> = ArrayList()
    val metadata: ElementMeta = this::class.java.getAnnotation(ElementMeta::class.java)
    var position: Position = Position.scaledPositioning(0.5f, 0.5f, 1f)
        private set

    @FloatSetting(name = "Scale", category = ["Display"], description = "How large the element is rendered.", min = 50f, max = 200f, suffix = "%", save = false)
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

        collectSettings()

        preloaded = true
        return this
    }

    // called after settings have loaded
    open fun init() {}
    // called when element is added
    open fun onAdded() {}
    // called when element is removed
    open fun onRemoved() {}

    abstract fun render(partialTicks: Float, renderOrigin: Int)

    abstract fun calculateHitBox(glScale: Float, drawScale: Float)
    protected open fun calculateHitBoxWidth(): Float = 10f
    protected open fun calculateHitBoxHeight(): Float = 10f

    fun resetSettings(save: Boolean = false) {
        position = Position.scaledPositioning(0.5f, 0.5f, 1f)

        for (s in settings) s.reset()
        if (save) {} // TODO: 16/07/2021 add saving
    }

    override fun generateJson(): JsonObjectExt {
        val json = JsonObjectExt()

        json["x"] = position.scaledX
        json["y"] = position.scaledY
        json["scale"] = position.scale
        val dynamic = JsonObjectExt()
        for (setting in settings) {
            if (!setting.shouldSave()) continue
            addSettingToJson(setting, dynamic)
        }
        json["dynamic"] = dynamic

        return json
    }

    override fun processJson(json: JsonObjectExt) {
        position.scaledX = json["x", position.scaledX]
        position.scaledY = json["y", position.scaledY]
        position.scale = json["scale", position.scale]

        val dynamic = json["dynamic", JsonObjectExt()]!!
        for (setting in settings) {
            var categoryJson = dynamic
            for (categoryName in setting.getCategory())
                categoryJson = categoryJson[categoryName, JsonObjectExt()]!!

            when (setting.jsonValue) {
                JsonValues.BOOLEAN -> setting.setJsonValue(categoryJson[setting.getNameJsonKey(), setting.getDefault() as Boolean])
                JsonValues.FLOAT -> setting.setJsonValue(categoryJson[setting.getNameJsonKey(), setting.getDefault() as Float])
                JsonValues.INT -> setting.setJsonValue(categoryJson[setting.getNameJsonKey(), setting.getDefault() as Int])
                JsonValues.STRING -> setting.setJsonValue(categoryJson[setting.getNameJsonKey(), setting.getDefault() as String]!!)
            }
        }
    }

    private fun collectSettings() {
        val classes = ArrayList<Class<*>>()
        var clazz: Class<*>? = this::class.java
        while (clazz != null) {
            classes.add(clazz)
            clazz = clazz.superclass
        }
        for (declaredClass in classes) {
            for (field in declaredClass.declaredFields) {
                LOGGER.info("Checking out ${field.name}")

                val boolean = field.getAnnotation(BooleanSetting::class.java)
                if (boolean != null)
                    settings.add(BooleanSettingWrapped(boolean, this, field))

                val int = field.getAnnotation(IntSetting::class.java)
                if (int != null)
                    settings.add(IntSettingWrapped(int, this, field))

                val float = field.getAnnotation(FloatSetting::class.java)
                if (float != null)
                    settings.add(FloatSettingWrapped(float, this, field))

                val string = field.getAnnotation(StringSetting::class.java)
                if (string != null)
                    settings.add(StringSettingWrapped(string, this, field))

                val stringList = field.getAnnotation(StringListSetting::class.java)
                if (string != null)
                    settings.add(StringListSettingWrapped(stringList, this, field))

                val enum = field.getAnnotation(EnumSetting::class.java)
                if (enum != null)
                    settings.add(EnumSettingWrapped(enum, this, field))

                val color = field.getAnnotation(ColorSetting::class.java)
                if (color != null)
                    settings.add(ColorSettingWrapped(color, this, field))
            }
        }

    }

}

@Target(AnnotationTarget.CLASS)
annotation class ElementMeta(val id: String, val name: String, val category: Array<String>, val description: String, val allowedVersions: Array<MCVersion> = [MCVersion.FORGE_1_8_9, MCVersion.FABRIC_1_17_1], val maxInstances: Int = Int.MAX_VALUE)