/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.config

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigObject
import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.compatibility.universal.LOGGER
import dev.isxander.evergreenhud.elements.ElementManager
import dev.isxander.evergreenhud.utils.*
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files

class ElementConfig(private val manager: ElementManager) {

    private var shouldSave = false

    fun save() {
        var data = ConfigFactory.empty()
            .withValue("schema", SCHEMA.asConfig())
            .root()

        val arr = arrayListOf<ConfigObject>()
        for (element in manager) {
            arr.add(ConfigFactory.empty()
                .withValue("id", manager.getElementId(element).asConfig())
                .withValue("data", element.conf).root())
        }
        data = data.withValue("elements", arr.asConfig())


        CONFIG_FILE.parentFile.mkdirs()
        Files.write(
            CONFIG_FILE.toPath(),
            data.toConfig().resolve().root().render(HoconUtils.niceRender).lines(),
            StandardCharsets.UTF_8
        )
        shouldSave = false
    }

    fun load() {
        if (!CONFIG_FILE.exists()) save().also { return@load }
        val json = attemptConversion(ConfigFactory.parseFile(CONFIG_FILE).root())

        manager.clearElements()
        val arr = json.toConfig().getObjectList("elements")
        for (elementData in arr) {
            val id = elementData.getOrDefault("id", "null".asConfig()).string()
            val element = manager.getNewElementInstance(id)

            if (element == null) {
                LOGGER.err("Found unknown element id ($id) in json! This probably means someone tampered with the json!")
                continue
            }

            element.preload().conf = elementData["data"]!!.obj()
            manager.addElement(element)
        }

        if (shouldSave) save()
    }

    @Suppress("UNUSED_EXPRESSION")
    private fun attemptConversion(hocon: ConfigObject): ConfigObject {
        val currentSchema = hocon.getOrDefault("schema", 0.asConfig()).int()

        // corrupt config. Reset
        if (currentSchema == 0 || currentSchema > MainConfig.SCHEMA) {
            return ConfigFactory.empty().root()
        }

        // there is no point recoding every conversion
        // when a new schema comes to be
        // so just convert the old conversions until done
        var convertedData = hocon
        var convertedSchema = currentSchema
        while (convertedSchema != MainConfig.SCHEMA) {
            LOGGER.info("Converting element configuration v$convertedSchema -> ${convertedSchema + 1}")
            when (convertedSchema) {

            }
            convertedSchema++
        }

        return convertedData
    }

    companion object {
        const val SCHEMA = 4
        val CONFIG_FILE: File
            get() = File(EvergreenHUD.profileManager.profileDirectory, "elements.conf")
    }

}