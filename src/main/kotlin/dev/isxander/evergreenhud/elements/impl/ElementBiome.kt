/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.annotations.ElementMeta
import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.mc
import net.minecraft.client.resource.language.I18n
import net.minecraft.util.registry.Registry

@ElementMeta(id = "BIOME", name = "Biome Display", description = "Displays the current biome you are in.", category = "World")
class ElementBiome : SimpleTextElement() {
    init {
        title = "Biome"
    }

    override fun calculateValue(): String {
        val player = mc.player ?: return "Unknown"

        val id = mc.world!!.registryManager!!.get(Registry.BIOME_KEY)!!.getId(mc.world?.getBiome(player.blockPos))!!
        return I18n.translate("biome.${id.namespace}.${id.path}")
    }
}
