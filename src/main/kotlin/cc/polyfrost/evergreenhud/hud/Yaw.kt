package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.evergreenhud.utils.decimalFormat
import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud
import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraft.util.MathHelper

class Yaw : Config(Mod("Yaw", ModType.HUD), "yaw.json") {
    @HUD(name = "Main")
    var hud = YawHud()

    init {
        initialize()
    }

    class YawHud : SingleTextHud("Yaw", false) {

        @Slider(name = "Accuracy", min = 0F, max = 10F)
        var accuracy = 2

        @Switch(name = "Trailing Zeros")
        var trailingZeros = true

        override fun getText(): String {
            return decimalFormat(accuracy, trailingZeros).format(mc.thePlayer?.rotationYaw?.let(MathHelper::wrapAngleTo180_float) ?: 0f)
        }
    }
}