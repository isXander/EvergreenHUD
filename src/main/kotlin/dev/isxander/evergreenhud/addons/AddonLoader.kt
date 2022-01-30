/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.addons

import dev.isxander.evergreenhud.utils.logger

class AddonLoader {
    val addons = arrayListOf<EvergreenAddonInfo>()

    fun invokePreinitEntrypoints() {
        for (addon in addons) {
            for (entrypoint in addon.entrypoints) {
                try {
                    entrypoint.onPreInitialize()
                } catch (e: Exception) {
                    logger.error("\n")
                    logger.error("<------------------------------------------------------------------->")
                    logger.error("Exception occurred in addon pre-init entrypoint: ${addon.name}")
                    e.printStackTrace()
                    logger.error("Please report this issue to ${addon.author}, not the")
                    logger.error("EvergreenHUD developer.")
                    logger.error("<------------------------------------------------------------------->")
                    logger.error("\n")
                }
            }
        }
    }

    fun invokeInitEntrypoints() {
        for (addon in addons) {
            for (entrypoint in addon.entrypoints) {
                try {
                    entrypoint.onInitialize()
                } catch (e: Exception) {
                    logger.error("\n")
                    logger.error("<------------------------------------------------------------------->")
                    logger.error("Exception occurred in addon init entrypoint: ${addon.name}")
                    e.printStackTrace()
                    logger.error("Please report this issue to ${addon.author}, not the")
                    logger.error("EvergreenHUD developer.")
                    logger.error("<------------------------------------------------------------------->")
                    logger.error("\n")
                }
            }
        }
    }
}
