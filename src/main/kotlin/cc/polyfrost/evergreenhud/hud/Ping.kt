package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.evergreenhud.utils.ServerPinger
import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.utils.dsl.mc

class Ping: Config(Mod("Ping", ModType.HUD), "ping.json") {
    @HUD(name = "Main")
    var hud = PingHud()

    init {
        initialize()
    }

    class PingHud : SingleTextHud("Ping", true) {

        @Slider(
            name = "Ping Period",
            min = 20F,
            max = 120F
        )
        var interval = 20

        @Switch(
            name = "Show in Single Player"
        )
        var showInSinglePlayer = true

        private val ping by ServerPinger.createListener({ interval * 20 }) { mc.currentServerData }

        @Transient private var example = false

        override fun drawExample(matrices: UMatrixStack?, x: Int, y: Int, scale: Float) {
            example = true
            try {
                super.drawExample(matrices, x, y, scale)
            } finally {
                example = false
            }
        }

        override fun draw(matrices: UMatrixStack?, x: Int, y: Int, scale: Float) {
            if (mc.isSingleplayer && !showInSinglePlayer && !example) return
            super.draw(matrices, x, y, scale)
        }

        override fun getText(): String {
            return ping?.toString() ?: "N/A"
        }

    }
}