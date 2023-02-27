package cc.polyfrost.evergreenhud

import cc.polyfrost.evergreenhud.hud.*
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = EvergreenHUD.MODID, name = EvergreenHUD.NAME, version = EvergreenHUD.VERSION)
class EvergreenHUD {
    @Mod.EventHandler
    fun onFMLInitialization(event: FMLInitializationEvent) {
        Armour()
        Biome()
        BlockAbove()
        Combo()
        Coordinates()
        CPS()
        Day()
        Direction()
        FPS()
        InGameTime()
        Memory()
        Ping()
        Pitch()
        PlaceCount()
        PlayerPreview()
        Reach()
        RealLifeTime()
        Saturation()
        ServerIP()
        Speed()
        Yaw()
    }

    companion object {
        const val MODID = "@ID@"
        const val NAME = "@NAME@"
        const val VERSION = "@VER@"
    }
}

class ClientDamageEntityEvent(val attacker: Entity, val target: Entity)
class ClientPlaceBlockEvent(val player: EntityPlayer, val world: World)
