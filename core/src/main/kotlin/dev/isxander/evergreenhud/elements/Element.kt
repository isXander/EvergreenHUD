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

import dev.isxander.evergreenhud.settings.ConfigProcessor
import dev.isxander.evergreenhud.settings.JsonValues
import dev.isxander.evergreenhud.settings.Setting
import dev.isxander.evergreenhud.settings.SettingAdapter
import dev.isxander.evergreenhud.settings.impl.*
import dev.isxander.evergreenhud.utils.JsonObjectExt
import dev.isxander.evergreenhud.utils.Position

abstract class Element : ConfigProcessor {

    val settings: MutableList<Setting<*, *>> = ArrayList()
    val metadata: ElementMeta = this::class.java.getAnnotation(ElementMeta::class.java)
    val position: Position = Position.scaledPositioning(0.5f, 0.5f, 1f)

    @FloatSetting(name = "Scale", category = "Display", description = "How large the element is rendered.", min = 50f, max = 200f, suffix = "%", save = false)
    var scale = SettingAdapter(100f)
        .set {
            position.scale = it / 100f
            return@set it
        }

    @BooleanSetting(name = "Show In Chat", category = "Visibility", description = "Whether or not element should be displayed in the chat menu. (Takes priority over show under gui)")
    var showInChat: Boolean = false
    @BooleanSetting(name = "Show In F3", category = "Visibility", description = "Whether or not element should be displayed when you have the debug menu open.")
    var showInDebug: Boolean = false
    @BooleanSetting(name = "Show Under GUIs", category = "Visibility", description = "Whether or not element should be displayed when you have a gui open.")
    var showUnderGui: Boolean = false

    init {
        collectSettings()
    }

    abstract fun render(partialTicks: Float, renderOrigin: Int)

    override fun generateJson(): JsonObjectExt {
        val json = JsonObjectExt()

        json.addProperty("x", position.scaledX)
        json.addProperty("y", position.scaledY)
        json.addProperty("scale", position.scale)
        val dynamic = JsonObjectExt()
        for (setting in settings) {
            if (!setting.shouldSave()) continue

            // TODO: why is this breaking
            // if (setting.getDefaultJsonValue() == setting.getJsonValue()) continue

            when (setting.jsonValue) {
                JsonValues.STRING -> dynamic.addProperty(setting.getJsonKey(), setting.getJsonValue() as String)
                JsonValues.BOOLEAN -> dynamic.addProperty(setting.getJsonKey(), setting.getJsonValue() as Boolean)
                JsonValues.FLOAT -> dynamic.addProperty(setting.getJsonKey(), setting.getJsonValue() as Float)
                JsonValues.INT -> dynamic.addProperty(setting.getJsonKey(), setting.getJsonValue() as Int)
            }
        }
        json.add("dynamic", dynamic)

        return json
    }

    override fun processJson(json: JsonObjectExt) {
        position.scaledX = json.optFloat("x")
        position.scaledY = json.optFloat("y")
        position.scale = json.optFloat("scale")

        val dynamic = json.optObject("dynamic")!!
        for (key in dynamic.keys) {
            for (setting in settings) {
                if (setting.getJsonKey() == key) {
                    when (setting.jsonValue) {
                        JsonValues.STRING -> setting.setJsonValue(dynamic.optString(key, setting.getDefaultJsonValue() as String)!!)
                        JsonValues.BOOLEAN -> setting.setJsonValue(dynamic.optBoolean(key, setting.getDefaultJsonValue() as Boolean))
                        JsonValues.FLOAT -> setting.setJsonValue(dynamic.optFloat(key, setting.getDefaultJsonValue() as Float))
                        JsonValues.INT -> setting.setJsonValue(dynamic.optInt(key, setting.getDefaultJsonValue() as Int))
                    }

                    break
                }
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
annotation class ElementMeta(val id: String, val name: String, val category: String, val description: String, val maxInstances: Int = Int.MAX_VALUE)