package dev.isxander.evergreenhud.compatibility.universal.impl.keybind

data class CustomKeybind(val key: Keyboard, val name: String, val category: String, val press: () -> Unit)

abstract class AIKeybindManager {

    abstract fun registerKeybind(keybind: CustomKeybind)

}