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

import co.uk.isxander.xanderlib.utils.Position
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject
import dev.isxander.evergreenhud.settings.*
import net.minecraft.client.gui.Gui


abstract class Element : Gui(), ConfigProcessor {

    val settings: MutableList<Setting<*, *>> = ArrayList()
    val metadata: ElementMeta = this::class.java.getAnnotation(ElementMeta::class.java)
    val position: Position = Position.getPositionWithScaledPositioning(0.5f, 0.5f, 1f)

    @FloatSetting(name = "Scale", category = "Display", description = "How large the element is rendered.", min = 50f, max = 200f, suffix = "%", save = false)
    var scale = 100f

    @BooleanSetting(name = "Show In Chat", category = "Visibility", description = "Whether or not element should be displayed in the chat menu. (Takes priority over show under gui)")
    var showInChat = false
    @BooleanSetting(name = "Show In F3", category = "Visibility", description = "Whether or not element should be displayed when you have the debug menu open.")
    var showInDebug = false
    @BooleanSetting(name = "Show Under GUIs", category = "Visibility", description = "Whether or not element should be displayed when you have a gui open.")
    var showUnderGui = false

    init {
        collectSettings()

    }

    private fun collectSettings() {
        val clazz = this::class.java
        for (field in clazz.declaredFields) {
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

//            val enum = field.getAnnotation(EnumSetting::class.java)
//            if (enum != null)
//                settings.add(EnumSetti)
        }
    }

    override fun generateJson(): BetterJsonObject {
        val json = BetterJsonObject()

        json.addProperty("x", position.xScaled)
        json.addProperty("y", position.yScaled)
        json.addProperty("scale", position.scale)
        val custom = BetterJsonObject()
        for (s in settings) {
            if (!s.shouldSave()) continue
            if (s is BooleanSettingWrapped) {
                // no need to save the default value
                if (s.getDefault() == s.get()) continue
                custom.addProperty(s.getJsonKey(), s.get())
            } else if (s is IntSettingWrapped) {
                // no need to save the default value
                if (s.getDefault() == s.get()) continue
                custom.addProperty(s.getJsonKey(), s.get())
            } else if (s is FloatSettingWrapped) {
                // no need to save the default value
                if (s.getDefault() == s.get()) continue
                custom.addProperty(s.getJsonKey(), s.get())
            } else if (s is StringListSettingWrapped) {
                // no need to save the default value
                if (s.get() == s.getDefault()) continue
                custom.addProperty(s.getJsonKey(), s.get())
            } else if (s is StringSettingWrapped) {
                // no need to save the default value
                if (s.get().equals(s.getDefault(), false)) continue
                custom.addProperty(s.getJsonKey(), s.get())
            } // else if (s is EnumSetting) {
//                val setting: EnumSetting<*> = s as EnumSetting<*>
//                // no need to save the default value
//                if (setting.getIndex() === setting.getDefaultIndex()) continue
//                custom.addProperty(setting.getJsonKey(), setting.getIndex())
//            }
        }
        json.add("dynamic", custom)

        return json
    }

    override fun processJson(json: BetterJsonObject) {

    }

}

@Target(AnnotationTarget.CLASS)
annotation class ElementMeta(val name: String, val category: String, val description: String, val maxInstances: Int = Int.MAX_VALUE)
