package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Exclude
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud
import cc.polyfrost.oneconfig.libs.universal.UMath
import cc.polyfrost.oneconfig.libs.universal.UMinecraft
import java.text.DecimalFormat

class Yaw : Config(Mod("Yaw", ModType.HUD), "yaw.json") {
    @HUD(name = "Main")
    var hud = YawHud()

    class YawHud : SingleTextHud("Yaw", false) {

        @Slider(name = "Accuracy", min = 0F, max = 10F)
        var accuracy = 2

        override fun getText(): String {
            UMinecraft.getPlayer()?.let {
                return DecimalFormat("0" + (if (accuracy > 0) "." else "") + repeat(accuracy))
                    .format(UMath.wrapAngleTo180(it.rotationYaw.toDouble()))
            }
            return ""
        }

        @Exclude
        companion object {
            private fun repeat(times: Int): String {
                val newString = StringBuilder()
                var i = 0
                while (times > i) {
                    ++i
                    newString.append("#")
                }
                return newString.toString()
            }
        }
    }
}