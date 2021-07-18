package dev.isxander.evergreenhud.config.profile

data class Profile(
    val id: String,
    val name: String,
    val description: String,
    val icon: DefaultIconProvider = DefaultIconProvider()
)

