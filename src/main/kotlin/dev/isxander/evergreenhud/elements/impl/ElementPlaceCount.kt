/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.event.ClientPlaceBlockEvent
import dev.isxander.evergreenhud.event.ClientTickEvent
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.int
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos

@ElementMeta(id = "PLACE_COUNT", name = "Block Place Count", description = "Displays the number of blocks you have placed in a certain interval.", category = "World")
class ElementPlaceCount : SimpleTextElement("Blocks") {
    var interval by int(1000) {
        name = "Interval"
        description = "The interval in which the number of blocks you have placed will be displayed."
        category = "Block Place Count"
    }

    private val blockCount = ArrayDeque<Long>()

    val clientTickEvent by event<ClientTickEvent> {
        val currentTime = System.currentTimeMillis()
        if (!blockCount.isEmpty()) {
            while ((currentTime - blockCount.first()) > interval) {
                blockCount.removeFirst()
                if (blockCount.isEmpty()) break
            }
        }
    }

    val clientPlaceBlockEvent by event<ClientPlaceBlockEvent>({ it.player == mc.player }) {
        blockCount.addLast(System.currentTimeMillis())
    }

    override fun calculateValue(): String {
        return blockCount.size.toString()
    }
}
