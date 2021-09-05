package dev.isxander.evergreenhud.utils

import com.electronwill.nightconfig.hocon.HoconFormat
import com.electronwill.nightconfig.json.JsonFormat

val hoconFormat = HoconFormat.instance()
val hoconParser = hoconFormat.createParser()
val hoconWriter = hoconFormat.createWriter()

val jsonFormat = JsonFormat.fancyInstance()
val jsonParser = jsonFormat.createParser()
val jsonWriter = jsonFormat.createWriter()