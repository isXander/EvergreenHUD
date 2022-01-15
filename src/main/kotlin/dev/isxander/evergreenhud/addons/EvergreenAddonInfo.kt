/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.addons

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.fabricmc.loader.api.metadata.ModMetadata

data class EvergreenAddonInfo(val id: String, val name: String, val author: String, val entrypoints: List<AddonInitializer>) {
    companion object {
        fun isAddon(metadata: ModMetadata) = metadata.containsCustomValue("evergreenhud")

        fun of(mod: ModContainer): EvergreenAddonInfo {
            val entrypoints = FabricLoader.getInstance().getEntrypointContainers("evergreenhud", AddonInitializer::class.java)
                .filter { it.provider == mod }
                .map { it.entrypoint }

            val metadata = mod.metadata
            val wrapper = metadata.getCustomValue("evergreenhud").asObject

            val id = wrapper["id"]?.asString ?: metadata.id
            val name = wrapper["name"]?.asString ?: metadata.name
            val author = wrapper["author"]?.asString ?: "Unknown"
            return EvergreenAddonInfo(id, name, author, entrypoints)
        }
    }
}
