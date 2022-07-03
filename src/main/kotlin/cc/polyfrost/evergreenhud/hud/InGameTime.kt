package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud
import cc.polyfrost.oneconfig.libs.universal.UMinecraft
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class InGameTime : Config(Mod("In Game Time", ModType.HUD), "evergreenhud/ingametime.json") {
    @HUD(name = "Main")
    var hud = InGameTimeHud()

    init {
        initialize()
    }

    class InGameTimeHud : SingleTextHud("Time", false) {

        @Switch(name = "Twelve Hour Time")
        var twelveHour = false

        override fun getText(): String {
            UMinecraft.getWorld()?.let {
                // ticks to ticks in day to seconds to millis plus six hours (time 0 = 6am)
                val date = Date(it.worldTime / 20 * 1000 + TimeUnit.HOURS.toMillis(6))
                return SimpleDateFormat(if (twelveHour) "hh:mm a" else "HH:mm")
                    .format(date).uppercase()
            }
            return "06:00 AM"
        }
    }
}