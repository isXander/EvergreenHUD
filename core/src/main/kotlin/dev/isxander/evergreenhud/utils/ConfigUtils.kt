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

package dev.isxander.evergreenhud.utils

import com.typesafe.config.*

val niceConfigRender = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false)!!

fun ConfigValue.int(): Int = this.unwrapped() as Int
fun ConfigValue.double(): Double = this.unwrapped() as Double
fun ConfigValue.float(): Float = (this.unwrapped() as Double).toFloat()
fun ConfigValue.string(): String = this.unwrapped() as String
fun ConfigValue.bool(): Boolean = this.unwrapped() as Boolean
fun ConfigValue.obj(): ConfigObject = this as ConfigObject

fun Int.asConfig(): ConfigValue = ConfigValueFactory.fromAnyRef(this)
fun Double.asConfig(): ConfigValue = ConfigValueFactory.fromAnyRef(this)
fun Float.asConfig(): ConfigValue = ConfigValueFactory.fromAnyRef(this.toDouble())
fun String.asConfig(): ConfigValue = ConfigValueFactory.fromAnyRef(this)
fun Boolean.asConfig(): ConfigValue = ConfigValueFactory.fromAnyRef(this)
fun Any?.asConfig(): ConfigValue = ConfigValueFactory.fromAnyRef(this)
fun MutableList<*>.asConfig(): ConfigValue = ConfigValueFactory.fromAnyRef(this)

fun Config.getFloat(path: String): Float = this.getDouble(path).toFloat()

@Suppress("UNCHECKED_CAST")
fun <T> Config.get(path: String): T = this.getAnyRef(path) as T
@Suppress("UNCHECKED_CAST")
fun <T> Config.getList(path: String): List<T> = this.getAnyRefList(path) as List<T>

fun <T> Config.getOrNull(path: String): T? = try { this.get<T>(path) } catch (e: ConfigException) { null }
fun <T> Config.getListOrNull(path: String): List<T>? = try { this.getList<T>(path) } catch (e: ConfigException) { null }
