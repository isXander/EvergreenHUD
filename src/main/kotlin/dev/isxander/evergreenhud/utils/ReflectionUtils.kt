/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

fun getCallerClass(thread: Thread = Thread.currentThread(), depth: Int = 0, ignoreInternalCalls: Boolean = false): Class<*> {
    check(!ignoreInternalCalls || depth - 3 >= thread.stackTrace.size ) { "Depth exceeds stack trace size!" }

    if (!ignoreInternalCalls) return Class.forName(thread.stackTrace[3 + depth].className)

    val internal = thread.stackTrace[2].className
    for (i in 3 + depth until thread.stackTrace.size) {
        val element = thread.stackTrace[i]

        if (element.className != internal)
            return Class.forName(element.className)
    }


    error("Could not find non-internal call within stacktrace!")
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
