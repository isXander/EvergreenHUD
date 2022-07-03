package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.utils.dsl.mc

class ServerIP: Config(Mod("Server IP", ModType.HUD), "evergreenhud/serverip.json") {
    @HUD(name = "Main")
    var hud = ServerIPHud()

    init {
        initialize()
    }

    class ServerIPHud: SingleTextHud("Server", false) {

        @Switch(
            name = "Show in Single Player"
        )
        var showInSinglePlayer = true

        @Text(name = "No Server Text")
        var noServerText = "127.0.0.1"

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
            if (mc.currentServerData == null && !showInSinglePlayer && !example) return
            super.draw(matrices, x, y, scale)
        }

        override fun getText(): String {
            return mc.currentServerData?.serverIP ?: noServerText
        }

    }
}