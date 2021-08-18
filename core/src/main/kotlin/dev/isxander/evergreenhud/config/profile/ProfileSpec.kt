package dev.isxander.evergreenhud.config.profile

import com.uchuhimo.konf.ConfigSpec

object ProfileSpec : ConfigSpec() {
    val schema by required<Int>()
    val current by required<String>()
    val profiles by required<List<Profile>>()
}