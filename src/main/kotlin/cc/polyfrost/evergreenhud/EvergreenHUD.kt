package cc.polyfrost.evergreenhud

import cc.polyfrost.oneconfig.config.Config
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import org.reflections.Reflections
import java.lang.reflect.InvocationTargetException

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
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
        }
        println("Took " + (System.currentTimeMillis() - time) + " milliseconds to initialize EvergreenHUD elements.")
    }

    companion object {
        const val MODID = "@ID@"
        const val NAME = "@NAME@"
        const val VERSION = "@VER@"
        private val reflections = Reflections("cc.polyfrost.evergreenhud.hud")
    }
}