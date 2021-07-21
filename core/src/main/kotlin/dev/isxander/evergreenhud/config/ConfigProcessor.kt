/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/gpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.config

import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.settings.JsonType
import dev.isxander.evergreenhud.settings.Setting
import dev.isxander.evergreenhud.settings.impl.*
import dev.isxander.evergreenhud.utils.JsonObjectExt
import java.awt.Color
import java.lang.ClassCastException
import kotlin.reflect.KProperty1
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmName

interface ConfigProcessor {

    var json: JsonObjectExt

    fun addSettingToJson(setting: Setting<*, *>, json: JsonObjectExt) {
        val category = setting.category

        var obj = json
        for (categoryEntry in category) {
            val newObj = obj[categoryEntry, JsonObjectExt()]!!
            obj[categoryEntry] = newObj
            obj = newObj
        }

        when (setting.jsonType) {
            JsonType.STRING -> obj[setting.nameJsonKey] = setting.jsonValue as String
            JsonType.BOOLEAN -> obj[setting.nameJsonKey] = setting.jsonValue as Boolean
            JsonType.FLOAT -> obj[setting.nameJsonKey] = setting.jsonValue as Float
            JsonType.INT -> obj[setting.nameJsonKey] = setting.jsonValue as Int
        }
    }

    fun setSettingFromJson(json: JsonObjectExt, setting: Setting<*, *>) {
        if (setting.readOnly) return
        when (setting.jsonType) {
            JsonType.BOOLEAN -> setting.jsonValue = json[setting.nameJsonKey, setting.defaultJsonValue as Boolean]
            JsonType.FLOAT -> setting.jsonValue = json[setting.nameJsonKey, setting.defaultJsonValue as Float]
            JsonType.INT -> setting.jsonValue = json[setting.nameJsonKey, setting.defaultJsonValue as Int]
            JsonType.STRING -> setting.jsonValue = json[setting.nameJsonKey, setting.defaultJsonValue as String]!!
        }
    }

    fun collectSettings(instance: Any, settingProcessor: (Setting<*, *>) -> Unit) {
        val classes = arrayListOf(instance::class)
        classes.addAll(instance::class.allSuperclasses)
        for (declaredClass in classes) {
            LOGGER.info(declaredClass.jvmName)
            for (property in declaredClass.declaredMemberProperties) {
                LOGGER.info("  ${property.name}")
                property.isAccessible = true
                for (declaredAnnotation in property.annotations) {
                    LOGGER.info("    ${declaredAnnotation.annotationClass.qualifiedName}")
                }

                @Suppress("UNCHECKED_CAST")
                try {
                    when {
                        property.hasAnnotation<BooleanSetting>() ->
                            settingProcessor.invoke(BooleanSettingWrapped(
                                property.findAnnotation()!!,
                                instance,
                                property as KProperty1<out Any, Boolean>
                            ))
                        property.hasAnnotation<ColorSetting>() ->
                            settingProcessor.invoke(ColorSettingWrapped(
                                property.findAnnotation()!!,
                                instance,
                                property as KProperty1<out Any, Color>
                            ))
                        property.hasAnnotation<OptionSetting>() ->
                            settingProcessor.invoke(OptionSettingWrapped(
                                property.findAnnotation()!!,
                                instance,
                                property as KProperty1<out Any, OptionContainer.Option>
                            ))
                        property.hasAnnotation<FloatSetting>() ->
                            settingProcessor.invoke(FloatSettingWrapped(
                                property.findAnnotation()!!,
                                instance,
                                property as KProperty1<out Any, Float>
                            ))
                        property.hasAnnotation<IntSetting>() ->
                            settingProcessor.invoke(IntSettingWrapped(
                                property.findAnnotation()!!,
                                instance,
                                property as KProperty1<out Any, Int>
                            ))
                        property.hasAnnotation<StringListSetting>() ->
                            settingProcessor.invoke(StringListSettingWrapped(
                                property.findAnnotation()!!,
                                instance,
                                property as KProperty1<out Any, String>
                            ))
                        property.hasAnnotation<StringSetting>() ->
                            settingProcessor.invoke(StringSettingWrapped(
                                property.findAnnotation()!!,
                                instance,
                                property as KProperty1<out Any, String>
                            ))
                    }
                } catch (e: ClassCastException) {
                    LOGGER.err("---------------------------")
                    LOGGER.err("FAILED TO COLLECT SETTING!")
                    LOGGER.err("Setting is incorrect type!")
                    LOGGER.err("Offendor: ${property.name}")
                    LOGGER.err("Class: ${declaredClass.qualifiedName ?: "UNKNOWN"}")
                    LOGGER.err("---------------------------")
                }


                // FIXME: 18/07/2021 ahhhh
//                for ((annotationClass, wrapped) in Setting.registeredSettings) {
//                    LOGGER.info("    ${annotation.name}, ${wrapped.name}")
//                    property.isAccessible = true
//
//                    if (property.hasAnnotation<annotationClass>())
//                    if (fieldAnnotation != null) {
//                        LOGGER.info("    Success!")
//                        settingProcessor.invoke(wrapped.getConstructor(annotation, Any::class.java, Field::class.java)
//                            .newInstance(fieldAnnotation, instance, field))
//                        break
//                    }
//                }
            }
        }
    }

}