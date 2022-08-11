package cc.polyfrost.evergreenhud

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.events.EventManager
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.reflections.Reflections
import java.io.File

@Mod(modid = EvergreenHUD.MODID, name = EvergreenHUD.NAME, version = EvergreenHUD.VERSION)
class EvergreenHUD {
    @Mod.EventHandler
    fun onFMLInitialization(event: FMLInitializationEvent?) {
        val time = System.currentTimeMillis()
        for (classes in reflections.getSubTypesOf(
            Config::class.java
        )) {
            try {
                classes.getConstructor().newInstance()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        println("Took " + (System.currentTimeMillis() - time) + " milliseconds to initialize EvergreenHUD elements.")
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onAttackEntity(event: AttackEntityEvent) {
        if (event.isCanceled) return
        EventManager.INSTANCE.post(ClientDamageEntityEvent(event.entityPlayer, event.target))
    }


    companion object {
        const val MODID = "@ID@"
        const val NAME = "@NAME@"
        const val VERSION = "@VER@"
        private val reflections = Reflections("cc.polyfrost.evergreenhud.hud")
    }
}

class ClientDamageEntityEvent(val attacker: Entity, val target: Entity)
class ClientPlaceBlockEvent(val player: EntityPlayer, val world: World)
