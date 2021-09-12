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

package dev.isxander.evergreenhud.api.fabric11701.impl

import dev.isxander.evergreenhud.api.impl.UWorld
import dev.isxander.evergreenhud.api.fabric11701.mc
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry

class WorldImpl : UWorld() {
    override val isNull: Boolean
        get() = mc.world == null

    override fun getBiomeAt(x: Int, y: Int, z: Int): String =
        mc.world!!.registryManager!!.get(Registry.BIOME_KEY)!!.getId(mc.world?.getBiome(BlockPos(x, y, z))).toString()

    override val time: Long
        get() = mc.world!!.timeOfDay
}