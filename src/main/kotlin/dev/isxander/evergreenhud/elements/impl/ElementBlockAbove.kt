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
import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.impl.int
import net.minecraft.block.*
import net.minecraft.util.BlockPos


@ElementMeta(id = "evergreenhud:block_above", name = "Block Above", description = "Show how many blocks until you hit your head on something above you.", category = "World")
class ElementBlockAbove : SimpleTextElement("Above") {
    var notify by boolean(true) {
        name = "Notify"
        description = "Make a sound when you get close to hitting your head."
        category = "Block Above"
    }
    var notifyHeight by int(3) {
        name = "Notify Height"
        description = "How close you need to be before you notify."
        category = "Block Above"
        range = 1..10

        depends { notify }
    }
    var checkHeight by int(10) {
        name = "Check Height"
        description = "How many blocks until element stops checking blocks."
        category = "Block Above"
        range = 1..30
    }

    private var notified = false

    override fun calculateValue(): String {
        if (mc.theWorld == null || mc.thePlayer == null) return "0"

        var above = 0
        for (i in 1..checkHeight) {
            val pos = BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1 + i, mc.thePlayer.posZ)
            if (pos.y > 255) break

            val state = mc.theWorld!!.getBlockState(pos) ?: continue
            if (state.block is BlockAir
                || state is BlockStaticLiquid
                || state.block is BlockSign
                || state is BlockVine
                || state.block is BlockBanner)
                continue

            above = i

            if (above <= notifyHeight && notify) {
                if (!notified) {
                    mc.thePlayer.playSound("random.orb", 0.1f, 0.5f)
                    notified = true
                }
            } else {
                notified = false
            }

            break
        }

        return above.toString()
    }
}
