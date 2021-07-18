package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.*
import dev.isxander.evergreenhud.event.RenderTickEvent
import gg.essential.elementa.font.DefaultFonts
import gg.essential.universal.ChatColor
import me.kbrewster.eventbus.Subscribe
import java.awt.Color
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.max
import kotlin.math.min

object Notifications {

    var WIDTH = 200
    var PADDING_WIDTH = 5
    var PADDING_HEIGHT = 3
    var TEXT_DISTANCE = 2

    private val notifications = ConcurrentLinkedQueue<NotifData>()

    init {
        EvergreenHUD.EVENT_BUS.register(this)
    }

    fun push(title: String, description: String, backColor: Color = Color(0, 0, 0), textColor: Color = Color(255, 255, 255), duration: Int = 20, consumer: () -> Unit = {}) =
        notifications.add(NotifData(title, description, backColor, textColor, duration, consumer))

    fun push(data: NotifData) =
        notifications.add(data.copy())

    @Subscribe
    fun onRender(event: RenderTickEvent) {
        if (notifications.isEmpty()) return

        val matrices = event.matrices
        val notif = notifications.first()
        var opacity = 200f

        if (notif.time <= 1 || notif.time >= 10) {
            val easeTime = min(notif.time, 1f)
            opacity = easeTime * 200
        }

        val boldedTitle = "${ChatColor.BOLD}${notif.title}"
        val wrappedTitle = StringUtils.wrapTextLinesFR(boldedTitle, WIDTH, " ")
        val wrappedDesc = StringUtils.wrapTextLinesFR(notif.description, WIDTH, " ")
        val textLines = wrappedTitle.size + wrappedDesc.size

        var rectWidth = MathUtils.lerp(notif.width, (WIDTH + (PADDING_WIDTH * 2f)), event.dt / 4f)
            .also { notif.width = it }

        if (notif.closing && notif.time <= 0.45f) {
            rectWidth = max(MathUtils.lerp(notif.width, -(WIDTH + (PADDING_WIDTH * 2f)), event.dt), 0f)
                .also { notif.width = it }
        }

        val rectHeight = (PADDING_HEIGHT * 2f) + (textLines * FONT_RENDERER.fontHeight) + ((textLines - 1) * TEXT_DISTANCE)
        val rectX = RESOLUTION.scaledWidth / 2f - (rectWidth / 2f)
        val rectY = RESOLUTION.scaledHeight * 0.0015625f // 3 px on a 1080p screen

        val mouseX = MOUSE_HELPER.mouseX
        val mouseY = MOUSE_HELPER.mouseY
        val mouseOver =
               mouseX >= rectX
            && mouseX <= rectX + rectWidth
            && mouseY >= rectY
            && mouseY <= rectY + rectHeight

        notif.mouseOverAdd = MathUtils.lerp(notif.mouseOverAdd, if (mouseOver) 40f else 0f, event.dt / 4f)
            .also { opacity += it }

        matrices.push()
        val clampedOpacity = MathUtils.clamp(opacity, 5f, 255f).toInt()
        notif.backColor = Color(notif.backColor.red, notif.backColor.green, notif.backColor.blue, clampedOpacity)
        notif.textColor = Color(notif.textColor.red, notif.textColor.green, notif.textColor.blue, clampedOpacity)
        GL.roundedRect(matrices, rectX, rectY, rectWidth, rectHeight, notif.backColor.rgb, 5f)

        if (notif.time > 0.1f) {
            GL.scissorStart(rectX.toInt(), rectY.toInt(), rectWidth.toInt(), rectHeight.toInt())
            var i = 0
            for (line in wrappedTitle) {
                GuiUtils.drawString(matrices, DefaultFonts.JETBRAINS_MONO_FONT_RENDERER, line, RESOLUTION.scaledWidth / 2f, rectY + PADDING_HEIGHT + (TEXT_DISTANCE * i) + FONT_RENDERER.fontHeight * i, notif.textColor, centered = true, shadow = true)
                i++
            }
            for (line in wrappedDesc) {
                GuiUtils.drawString(matrices, DefaultFonts.JETBRAINS_MONO_FONT_RENDERER, line, RESOLUTION.scaledWidth / 2f, rectY + PADDING_HEIGHT + (TEXT_DISTANCE * i) + FONT_RENDERER.fontHeight * i, notif.textColor, centered = true, shadow = true)
                i++
            }
            GL.scissorEnd()
        }

        matrices.pop()

        if (notif.time >= (if (notif.duration == -1) 5 else notif.duration)) {
            notif.closing = true
        }
        if (!notif.clicked && mouseOver && MOUSE_HELPER.wasLeftMouseDown) {
            notif.clicked = true
            notif.consumer.invoke()
            notif.closing = true
            if (notif.time > 1f) {
                notif.time = 1f
            }
        }
        if (!mouseOver) {
            notif.time += (if (notif.closing) -0.2f else 0.2f) * event.dt
        }
        if (notif.closing && notif.time <= 0) {
            notifications.remove()
        }
    }

}

data class NotifData(val title: String, val description: String, var backColor: Color = Color(0, 0, 0), var textColor: Color = Color(255, 255, 255), val duration: Int = 20, val consumer: () -> Unit = {}) {
    var time = 0f
    var width = 0f
    var mouseOverAdd = 0f
    var closing = false
    var clicked = false
}

