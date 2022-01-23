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
import net.minecraft.block.AbstractBannerBlock
import net.minecraft.block.Blocks
import net.minecraft.block.SignBlock
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents

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
        if (mc.world == null || mc.player == null) return "0"

        var above = 0
        for (i in 1..checkHeight) {
            val pos = mc.player!!.blockPos.add(0, i + 1, 0)
            if (pos.y > mc.world!!.topY) break

            val state = mc.world!!.getBlockState(pos) ?: continue
            if (state.isOf(Blocks.AIR)
                || state.isOf(Blocks.WATER)
                || state.block is SignBlock
                || state.isOf(Blocks.WEEPING_VINES)
                || state.isOf(Blocks.VINE)
                || state.block is AbstractBannerBlock)
                continue

            above = i

            if (above <= notifyHeight && notify) {
                if (!notified) {
                    mc.player!!.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1f, 1f)
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
