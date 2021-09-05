package dev.isxander.evergreenhud.utils

import com.electronwill.nightconfig.json.JsonFormat
import com.electronwill.nightconfig.toml.TomlFormat

val tomlFormat = TomlFormat.instance()
val tomlParser = tomlFormat.createParser()
val tomlWriter = tomlFormat.createWriter()

val jsonFormat = JsonFormat.fancyInstance()
val jsonParser = jsonFormat.createParser()
val jsonWriter = jsonFormat.createWriter()