/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.addons

import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.utils.logger
import net.fabricmc.loader.api.FabricLoader
import kotlin.io.path.inputStream

class AddonLoader {
    val addons = FabricLoader.getInstance().allMods
        .filter { EvergreenAddonInfo.isAddon(it.metadata) }
        .associateWith { EvergreenAddonInfo.of(it) }

    fun addSources(elementManager: ElementManager) {
        for ((mod, addon) in addons) {
            val elements = mod.getPath("evergreenhud-elements.json")
            elementManager.addSource(elements.inputStream(), addon.id)
        }
    }

    fun invokePreinitEntrypoints() {
        for (addon in addons.values) {
            for (entrypoint in addon.entrypoints) {
                try {
                    entrypoint.onPreInitialize()
                } catch (e: Exception) {
                    logger.error('\n')
                    logger.error("<------------------------------------------------------------------->")
                    logger.error("Exception occurred in addon pre-init entrypoint: ${addon.name}")
                    e.printStackTrace()
                    logger.error("Please report this issue to ${addon.author}, not the")
                    logger.error("EvergreenHUD developer.")
                    logger.error("<------------------------------------------------------------------->")
                    logger.error('\n')
                }
            }
        }
    }

    fun invokeInitEntrypoints() {
        for (addon in addons.values) {
            for (entrypoint in addon.entrypoints) {
                try {
                    entrypoint.onInitialize()
                } catch (e: Exception) {
                    logger.error('\n')
                    logger.error("<------------------------------------------------------------------->")
                    logger.error("Exception occurred in addon init entrypoint: ${addon.name}")
                    e.printStackTrace()
                    logger.error("Please report this issue to ${addon.author}, not the")
                    logger.error("EvergreenHUD developer.")
                    logger.error("<------------------------------------------------------------------->")
                    logger.error('\n')
                }
            }
        }
    }
}
