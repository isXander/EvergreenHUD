package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud
import cc.polyfrost.oneconfig.utils.dsl.mc

class Day: Config(Mod("Day", ModType.HUD), "evergreenhud/day.json", false) {
    @HUD(name = "Main")
    var hud = DayHud()

    init {
        initialize()
    }

    class DayHud : SingleTextHud("Day", true, 400, 30) {
        override fun getText(example: Boolean): String {
            if (mc.theWorld == null) return "0"
            return (mc.theWorld.worldTime / 24000L).toString()
        }
    }
}