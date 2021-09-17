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

package dev.isxander.evergreenhud.api.forge10809.impl

import dev.isxander.evergreenhud.api.impl.ULoader
import net.minecraftforge.fml.common.Loader
import java.net.URL
import java.net.URLClassLoader

class LoaderImpl : ULoader() {
    override fun isModLoaded(id: String): Boolean =
        Loader.isModLoaded(id)

    override fun addURL(url: URL): Boolean {
        try {
            val classLoader = LoaderImpl::class.java.classLoader
            if (classLoader is URLClassLoader) {
                classLoader::class.java.getDeclaredMethod("addURL", URL::class.java)
                    .invoke(classLoader, url)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }
}
