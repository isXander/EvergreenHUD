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

package dev.isxander.evergreenhud.config

import com.typesafe.config.ConfigObject
import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.settings.HoconType
import dev.isxander.evergreenhud.settings.Setting
import dev.isxander.evergreenhud.settings.impl.*
import dev.isxander.evergreenhud.utils.*
import java.awt.Color
import java.lang.ClassCastException
import kotlin.reflect.KProperty1
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

interface ConfigProcessor {

    var conf: ConfigObject

    fun addSettingToConfig(setting: Setting<*, *>, data: ConfigObject): ConfigObject {
        val category = setting.category

        var obj = data.toConfig()
        val path = category.joinToString(separator = ".", postfix = ".${setting.nameSerializedKey}")

        obj = when (setting.hoconType) {
            HoconType.STRING -> obj.withValue(path, (setting.serializedValue as String).asConfig())
            HoconType.BOOLEAN -> obj.withValue(path, (setting.serializedValue as Boolean).asConfig())
            HoconType.FLOAT -> obj.withValue(path, (setting.serializedValue as Float).asConfig())
            HoconType.INT -> obj.withValue(path, (setting.serializedValue as Int).asConfig())
        }

        return obj.root()
    }

    fun setSettingFromConfig(data: ConfigObject, setting: Setting<*, *>) {
        if (setting.readOnly) return
        when (setting.hoconType) {
            HoconType.BOOLEAN -> setting.serializedValue = data.getOrDefault(setting.nameSerializedKey, (setting.defaultSerializedValue as Boolean).asConfig()).bool()
            HoconType.FLOAT -> setting.serializedValue = data.getOrDefault(setting.nameSerializedKey, (setting.defaultSerializedValue as Float).asConfig()).float()
            HoconType.INT -> setting.serializedValue = data.getOrDefault(setting.nameSerializedKey, (setting.defaultSerializedValue as Int).asConfig()).int()
            HoconType.STRING -> setting.serializedValue = data.getOrDefault(setting.nameSerializedKey, (setting.defaultSerializedValue as String).asConfig()).string()
        }
    }

    fun collectSettings(instance: Any, settingProcessor: (Setting<*, *>) -> Unit) {
        val classes = arrayListOf(instance::class)
        classes.addAll(instance::class.allSuperclasses)
        for (declaredClass in classes) {
            for (property in declaredClass.declaredMemberProperties) {
                property.isAccessible = true

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
                    LOGGER.err("Offender: ${property.name}")
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