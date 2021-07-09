/*
 * Copyright (C) isXander [2019 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package co.uk.isxander.evergreenhud.elements

import co.uk.isxander.evergreenhud.config.ElementConfig
import co.uk.isxander.evergreenhud.config.MainConfig
import co.uk.isxander.evergreenhud.elements.impl.*
import co.uk.isxander.evergreenhud.settings.*
import co.uk.isxander.xanderlib.utils.Constants.*
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject
import lombok.Getter
import lombok.Setter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ElementManager : ConfigProcessor {

    /**
     * all registered elements
     */
    private val availableElements: MutableMap<String, Class<out Element>> = HashMap()
    /**
     * @return the elements that are currently being rendered
     */
    @Getter private val currentElements: List<Element> = ArrayList()

    /* Config */
    @Getter private val mainConfig: MainConfig = MainConfig(this)
    @Getter private val elementConfig: ElementConfig = ElementConfig(this)

    /* Settings */
    @Getter private val settings: MutableList<Setting<*, *>> = ArrayList()

    init {
        registerDefaultElements()
    }

    private fun registerDefaultElements() {
        registerElement("TEST_ELEMENT", TestElement::class.java)
    }

    fun registerElement(id: String, element: Class<out Element>) {
        availableElements[id] = element
    }

    fun getAvailableElements(): Map<String, Class<out Element>> {
        return Collections.unmodifiableMap(availableElements)
    }

    override fun generateJson(): BetterJsonObject {
        val json = BetterJsonObject()

        for (setting in settings) {
            if (!setting.shouldSave()) continue

            if (setting.getDefaultJsonValue() == setting.getJsonValue()) continue

            when (setting.jsonValue) {
                JsonValues.STRING -> json.addProperty(setting.getJsonKey(), setting.getJsonValue() as String)
                JsonValues.BOOLEAN -> json.addProperty(setting.getJsonKey(), setting.getJsonValue() as Boolean)
                JsonValues.FLOAT -> json.addProperty(setting.getJsonKey(), setting.getJsonValue() as Float)
                JsonValues.INT -> json.addProperty(setting.getJsonKey(), setting.getJsonValue() as Int)
            }
        }

        return json
    }

    override fun processJson(json: BetterJsonObject) {
        for (key in json.allKeys) {
            for (setting in settings) {
                if (setting.getJsonKey() == key) {
                    when (setting.jsonValue) {
                        JsonValues.STRING -> setting.setJsonValue(json.optString(key, setting.getDefaultJsonValue() as String))
                        JsonValues.BOOLEAN -> setting.setJsonValue(json.optBoolean(key, setting.getDefaultJsonValue() as Boolean))
                        JsonValues.FLOAT -> setting.setJsonValue(json.optFloat(key, setting.getDefaultJsonValue() as Float))
                        JsonValues.INT -> setting.setJsonValue(json.optInt(key, setting.getDefaultJsonValue() as Int))
                    }

                    break
                }
            }
        }
    }

}