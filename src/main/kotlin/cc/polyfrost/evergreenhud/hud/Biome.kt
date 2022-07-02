package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud
import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraft.util.BlockPos

class Biome: Config(Mod("Biome", ModType.HUD), "biome.json") {
    @HUD(name = "Main")
    var hud = BiomeHud()

    init {
        initialize()
    }

    class BiomeHud: SingleTextHud("Biome", true) {
        override fun getText(): String {
            val player = mc.thePlayer ?: return "Unknown"

            val playerPos = BlockPos(player.posX, player.posY, player.posZ)
            val playerChunk = mc.theWorld.getChunkFromBlockCoords(playerPos)

            return playerChunk.getBiome(playerPos, mc.theWorld.worldChunkManager).biomeName
        }

    }
}