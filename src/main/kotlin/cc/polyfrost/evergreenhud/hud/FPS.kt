package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud
import net.minecraft.client.Minecraft

class FPS: Config(Mod("FPS", ModType.HUD), "fps.json") {
    @HUD(name = "Main")
    var hud = FPSHud()

    class FPSHud : SingleTextHud("FPS", true) {
        override fun getText(): String {
            return Minecraft.getDebugFPS().toString()
        }
    }
}