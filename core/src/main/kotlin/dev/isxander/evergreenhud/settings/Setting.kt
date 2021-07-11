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

import java.lang.IllegalArgumentException
import java.lang.reflect.Field

@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class BooleanSetting(val name: String, val category: String, val description: String, val save: Boolean = true)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class IntSetting(val name: String, val category: String, val description: String, val min: Int, val max: Int, val suffix: String = "", val save: Boolean = true)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class FloatSetting(val name: String, val category: String, val description: String, val min: Float, val max: Float, val suffix: String = "", val save: Boolean = true)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class StringListSetting(val name: String, val category: String, val description: String, val defaultIndex: Int, val save: Boolean = true)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class EnumSetting(val name: String, val category: String, val description: String, val save: Boolean = true)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class StringSetting(val name: String, val category: String, val description: String, val save: Boolean = true)

abstract class Setting<T, E>(val annotation: E, val annotatedObject: Any, val annotatedField: Field, val jsonValue: JsonValues) {

    init {
        annotatedField.isAccessible = true
    }

    abstract fun getName(): String
    abstract fun getCategory(): String
    abstract fun getDescription(): String
    abstract fun shouldSave(): Boolean

    abstract fun get(): T
    abstract fun set(new: T)
    abstract fun setJsonValue(new: Any)
    abstract fun getJsonValue(): Any
    abstract fun getDefault(): T
    abstract fun getDefaultJsonValue(): Any

    fun reset() {
        set(getDefault())
    }
    fun getJsonKey(): String {
        return getName().replace(" ", "")
    }
}

class BooleanSettingWrapped(annotation: BooleanSetting, annotatedObject: Any, annotatedField: Field) : Setting<Boolean, BooleanSetting>(annotation, annotatedObject, annotatedField, JsonValues.BOOLEAN) {
    private val default: Boolean = get()

    override fun getName(): String = annotation.name
    override fun getCategory(): String = annotation.category
    override fun getDescription(): String = annotation.description
    override fun shouldSave(): Boolean = annotation.save

    override fun get(): Boolean {
        return try { annotatedField.getBoolean(annotatedObject) }
        catch (e: IllegalArgumentException) { (annotatedField.get(annotatedObject) as BooleanSettingWrapped).get() }
    }

    override fun set(new: Boolean) {
        try { annotatedField.setBoolean(annotatedObject, new) }
        catch (e: IllegalArgumentException) { (annotatedField.get(annotatedObject) as BooleanSettingWrapped).set(new) }
    }
    override fun setJsonValue(new: Any) = set(new as Boolean)

    override fun getJsonValue(): Boolean {
        return get()
    }

    override fun getDefault(): Boolean = default
    override fun getDefaultJsonValue(): Boolean = default
}
class IntSettingWrapped(annotation: IntSetting, annotationObject: Any, annotatedField: Field) : Setting<Int, IntSetting>(annotation, annotationObject, annotatedField, JsonValues.INT) {
    private val default: Int = get()

    override fun getName(): String = annotation.name
    override fun getCategory(): String = annotation.category
    override fun getDescription(): String = annotation.description
    override fun shouldSave(): Boolean = annotation.save

    override fun get(): Int {
        return try { annotatedField.getInt(annotatedObject) }
        catch (e: IllegalArgumentException) { (annotatedField.get(annotatedObject) as IntSettingWrapped).get() }
    }

    override fun set(new: Int) {
        try { annotatedField.setInt(annotatedObject, new) }
        catch (e: IllegalArgumentException) { (annotatedField.get(annotatedObject) as IntSettingWrapped).set(new) }
    }
    override fun setJsonValue(new: Any) = set(new as Int)

    override fun getDefault(): Int = default
    override fun getDefaultJsonValue(): Int = default

    override fun getJsonValue(): Int {
        return get()
    }
}
class FloatSettingWrapped(annotation: FloatSetting, annotationObject: Any, annotatedField: Field) : Setting<Float, FloatSetting>(annotation, annotationObject, annotatedField, JsonValues.FLOAT) {
    private val default: Float = get()

    override fun getName(): String = annotation.name
    override fun getCategory(): String = annotation.category
    override fun getDescription(): String = annotation.description
    override fun shouldSave(): Boolean = annotation.save

    override fun get(): Float {
        return try { annotatedField.getFloat(annotatedObject) }
        catch (e: IllegalArgumentException) { (annotatedField.get(annotatedObject) as FloatSettingWrapped).get() }
    }

    override fun set(new: Float) {
        try { annotatedField.setFloat(annotatedObject, new) }
        catch (e: IllegalArgumentException) { (annotatedField.get(annotatedObject) as FloatSettingWrapped).set(new) }
    }
    override fun setJsonValue(new: Any) = set(new as Float)

    override fun getDefault(): Float = default
    override fun getDefaultJsonValue(): Float = default

    override fun getJsonValue(): Float {
        return get()
    }
}
class StringListSettingWrapped(annotation: StringListSetting, annotationObject: Any, annotatedField: Field) : Setting<Int, StringListSetting>(annotation, annotationObject, annotatedField, JsonValues.INT) {
    private val options: List<String> = annotatedField.get(annotationObject) as List<String>
    private var index: Int = annotation.defaultIndex
    private val defaultIndex: Int = index

    override fun getName(): String = annotation.name
    override fun getCategory(): String = annotation.category
    override fun getDescription(): String = annotation.description
    override fun shouldSave(): Boolean = annotation.save

    override fun get(): Int {
        return index
    }
    fun getString(): String {
        return options[index]
    }

    override fun set(new: Int) {
        index = new
    }
    override fun setJsonValue(new: Any) = set(new as Int)
    fun setString(new: String) {
        index = options.indexOf(new)
    }

    override fun getDefault(): Int = defaultIndex
    override fun getDefaultJsonValue(): Int = defaultIndex

    override fun getJsonValue(): Int {
        return get()
    }
}
class StringSettingWrapped(annotation: StringSetting, annotationObject: Any, annotatedField: Field) : Setting<String, StringSetting>(annotation, annotationObject, annotatedField, JsonValues.STRING) {
    private val defaultString: String = get()

    override fun getName(): String = annotation.name
    override fun getCategory(): String = annotation.category
    override fun getDescription(): String = annotation.description
    override fun shouldSave(): Boolean = annotation.save

    override fun get(): String {
        val obj = annotatedField.get(annotatedObject)
        if (obj is String) return obj
        return (obj as StringSettingWrapped).get()
    }

    override fun set(new: String) {
        if (String::class.java.isAssignableFrom(annotatedField.type)) annotatedField.set(annotatedObject, new)
        (annotatedField.get(annotatedObject) as StringSettingWrapped).set(new)
    }
    override fun setJsonValue(new: Any) = set(new as String)

    override fun getJsonValue(): String {
        return get()
    }

    override fun getDefault(): String = get()
    override fun getDefaultJsonValue(): String = get()

}


