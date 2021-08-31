package dev.isxander.evergreenhud.repo

import com.uchuhimo.konf.ConfigSpec
import dev.isxander.evergreenhud.EvergreenHUD

object ResponseSpec : ConfigSpec() {
    val blacklisted by optional<List<String>>(listOf())
    val latest by optional<String>(EvergreenHUD.VERSION_STR)
}