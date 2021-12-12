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

package dev.isxander.evergreenhud.utils

fun getCallerClass(thread: Thread = Thread.currentThread(), depth: Int = 0, ignoreInternalCalls: Boolean = false): Class<*> {
    if (!ignoreInternalCalls) return Class.forName(thread.stackTrace[3 + depth].className)

    if (depth - 3 >= thread.stackTrace.size) throw IllegalArgumentException("Depth exceeds stack trace size!")

    val internal = thread.stackTrace[2].className
    for (i in 3 + depth until thread.stackTrace.size) {
        val element = thread.stackTrace[i]

        if (element.className != internal)
            return Class.forName(element.className)
    }


    throw IllegalStateException("Could not find non-internal call within stacktrace!")
}

var vmVersion: Int = -1
    get() {
        if (field < 0) {
            field = System.getProperty("java.class.version")?.let { (it.toFloat() - 44).toInt() }
                ?: System.getProperty("java.vm.specification.version")?.substringAfterLast('.')?.toInt()
                ?: 8
        }
        return field
    }
    private set
