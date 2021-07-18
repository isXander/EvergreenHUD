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

package dev.isxander.evergreenhud.settings

import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.settings.impl.*
import dev.isxander.evergreenhud.utils.JsonObjectExt
import java.lang.reflect.Field
import java.util.ArrayList
import kotlin.reflect.KClass
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
        when (setting.jsonType) {
            JsonType.BOOLEAN -> setting.jsonValue = json[setting.nameJsonKey, setting.default as Boolean]
            JsonType.FLOAT -> setting.jsonValue = json[setting.nameJsonKey, setting.default as Float]
            JsonType.INT -> setting.jsonValue = json[setting.nameJsonKey, setting.default as Int]
            JsonType.STRING -> setting.jsonValue = json[setting.nameJsonKey, setting.default as String]!!
        }
    }

    fun collectSettings(instance: Any, settingProcessor: (Setting<*, *>) -> Unit) {
        val classes = arrayListOf(instance::class)
        classes.addAll(instance::class.allSuperclasses)
        for (declaredClass in classes) {
            LOGGER.info(declaredClass.jvmName)
            for (field in declaredClass.declaredMemberProperties) {
                LOGGER.info("  ${field.name}")
                field.isAccessible = true
                for (declaredAnnotation in field.annotations) {
                    LOGGER.info("    ${declaredAnnotation.annotationClass.qualifiedName}")
                }

                when {
                    field.hasAnnotation<BooleanSetting>() ->
                        settingProcessor.invoke(BooleanSettingWrapped(field.findAnnotation()!!, instance, field.javaField!!))
                    field.hasAnnotation<ColorSetting>() ->
                        settingProcessor.invoke(ColorSettingWrapped(field.findAnnotation()!!, instance, field.javaField!!))
                    field.hasAnnotation<OptionSetting>() ->
                        settingProcessor.invoke(OptionSettingWrapped(field.findAnnotation()!!, instance, field.javaField!!))
                    field.hasAnnotation<FloatSetting>() ->
                        settingProcessor.invoke(FloatSettingWrapped(field.findAnnotation()!!, instance, field.javaField!!))
                    field.hasAnnotation<IntSetting>() ->
                        settingProcessor.invoke(IntSettingWrapped(field.findAnnotation()!!, instance, field.javaField!!))
                    field.hasAnnotation<StringListSetting>() ->
                        settingProcessor.invoke(StringListSettingWrapped(field.findAnnotation()!!, instance, field.javaField!!))
                    field.hasAnnotation<StringSetting>() ->
                        settingProcessor.invoke(StringSettingWrapped(field.findAnnotation()!!, instance, field.javaField!!))
                }

                // FIXME: 18/07/2021 ahhhh
//                for ((annotation, wrapped) in Setting.registeredSettings) {
//                    LOGGER.info("    ${annotation.name}, ${wrapped.name}")
//                    field.isAccessible = true
//                    val fieldAnnotation = field.getAnnotation(annotation)
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