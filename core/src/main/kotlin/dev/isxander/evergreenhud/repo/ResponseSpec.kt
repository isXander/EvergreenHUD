package dev.isxander.evergreenhud.repo

import com.uchuhimo.konf.ConfigSpec
import dev.isxander.evergreenhud.EvergreenInfo

object ResponseSpec : ConfigSpec() {
    val blacklisted by optional<List<String>>(listOf())
    val latest by optional<String>(EvergreenInfo.VERSION_FULL.toString())
}