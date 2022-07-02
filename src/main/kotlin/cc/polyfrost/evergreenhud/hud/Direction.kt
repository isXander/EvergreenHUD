package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.evergreenhud.utils.Facing
import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud
import cc.polyfrost.oneconfig.utils.dsl.mc

class Direction: Config(Mod("Direction", ModType.HUD), "direction.json") {
    @HUD(name = "Main")
    var hud = DirectionHud()

    init {
        initialize()
    }

    class DirectionHud: SingleTextHud("Direction", true) {

        @Switch(
            name = "Abbreviated"
        )
        var abbreviated = false

        override fun getText(): String {
            if (mc.thePlayer == null) return "Unknown"

            val facing = Facing.parse(mc.thePlayer!!.rotationYaw)
            return if (abbreviated) facing.abbreviated else facing.full
        }

    }
}