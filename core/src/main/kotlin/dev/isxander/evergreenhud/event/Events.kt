package dev.isxander.evergreenhud.event

import gg.essential.universal.UMatrixStack

class ClientTickEvent
class RenderHUDEvent(val dt: Float, val matrices: UMatrixStack)
class RenderTickEvent(val dt: Float, val matrices: UMatrixStack)