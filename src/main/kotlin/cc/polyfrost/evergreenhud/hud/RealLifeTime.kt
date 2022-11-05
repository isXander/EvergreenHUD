package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud
import java.text.SimpleDateFormat
import java.util.*

class RealLifeTime : Config(Mod("IRL Time", ModType.HUD), "evergreenhud/irltime.json", false) {
    @HUD(name = "Main")
    var hud = RealLifeTimeHud()

    init {
        initialize()
    }

    class RealLifeTimeHud : SingleTextHud("Time", true, 120, 10) {

        @Switch(name = "Twelve Hour Time")
        var twelveHour = false

        @Switch(name = "Seconds")
        var seconds = false

        override fun getText(example: Boolean): String = SimpleDateFormat(String.format(if (twelveHour) "hh:mm%s a" else "HH:mm%s", if (seconds) ":ss" else ""))
            .format(Calendar.getInstance().time).uppercase()
    }
}