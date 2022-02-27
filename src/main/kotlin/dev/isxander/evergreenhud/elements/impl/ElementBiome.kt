/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import net.minecraft.client.resource.language.I18n
import net.minecraft.util.registry.Registry

@ElementMeta(id = "evergreenhud:biome", name = "Biome Display", description = "Displays the current biome you are in.", category = "World")
class ElementBiome : SimpleTextElement("Biome") {
    override fun calculateValue(): String {
        val player = mc.player ?: return "Unknown"

        val id = mc.world!!.registryManager!!.get(Registry.BIOME_KEY)!!.getId(mc.world?.getBiome(player.blockPos)?.value())!!
        return I18n.translate("biome.${id.namespace}.${id.path}")
    }
}
