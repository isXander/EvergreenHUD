/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils

import java.awt.Desktop
import java.io.IOException
import java.net.URI

/**
 * This code is taken from
 * https://github.com/Sk1erLLC/UniversalCraft
 *
 * (thanks guys)
 */
object UDesktop {
    var isLinux: Boolean = false
        private set

    var isXdg: Boolean = false
        private set

    var isKde: Boolean = false
        private set

    var isGnome: Boolean = false
        private set

    var isMac: Boolean = false
        private set

    var isWindows: Boolean = false
        private set

    init {
        val osName = try {
            System.getProperty("os.name")
        } catch (e: SecurityException) {
            null
        }
        isLinux = osName != null && (osName.startsWith("Linux") || osName.startsWith("LINUX"))
        isMac = osName != null && osName.startsWith("Mac")
        isWindows = osName != null && osName.startsWith("Windows")
        if (isLinux) {
            System.getenv("XDG_SESSION_ID")?.let {
                isXdg = it.isNotEmpty()
            }
            System.getenv("GDMSESSION")?.lowercase()?.let {
                isGnome = "gnome" in it
                isKde = "kde" in it
            }
        } else {
            isXdg = false
            isKde = false
            isGnome = false
        }
    }

    fun browse(uri: URI): Boolean = browseDesktop(uri) || openSystemSpecific(uri.toString())

    private fun openSystemSpecific(file: String): Boolean {
        return when {
            isLinux -> when {
                isXdg -> runCommand("xdg-open \"$file\"")
                isKde -> runCommand("kde-open \"$file\"")
                isGnome -> runCommand("gnome-open \"$file\"")
                else -> runCommand("kde-open \"$file\"") || runCommand("gnome-open \"$file\"")
            }
            isMac -> runCommand("open \"$file\"")
            isWindows -> runCommand("explorer \"$file\"")
            else -> false
        }
    }

    private fun browseDesktop(uri: URI): Boolean {
        return if (!Desktop.isDesktopSupported()) false else try {
            if (!Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                if (isLinux) {
                    return when {
                        isXdg -> runCommand("xdg-open $uri")
                        isKde -> runCommand("kde-open $uri")
                        isGnome -> runCommand("gnome-open $uri")
                        else -> runCommand("kde-open $uri") || runCommand("gnome-open $uri")
                    }
                }
                return false
            }

            Desktop.getDesktop().browse(uri)
            true
        } catch (e: Throwable) {
            false
        }
    }

    private fun runCommand(command: String): Boolean {
        return try {
            Runtime.getRuntime().exec(command).let {
                it != null && it.isAlive
            }
        } catch (e: IOException) {
            false
        }
    }
}
