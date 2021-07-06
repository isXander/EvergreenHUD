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

import co.uk.isxander.evergreenhud.EvergreenHUD
import co.uk.isxander.evergreenhud.config.ElementConfig
import co.uk.isxander.evergreenhud.config.MainConfig
import co.uk.isxander.evergreenhud.settings.*
import co.uk.isxander.xanderlib.utils.Constants.*
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject
import lombok.Getter
import lombok.Setter

class ElementManager : ConfigProcessor {

    /**
     * all registered elements
     */
    private val availableElements: Map<String, Class<in Element>> = HashMap()
    /**
     * @return the elements that are currently being rendered
     */
    @Getter private val currentElements: List<Element> = ArrayList()

    /* Config */
    @Getter private val mainConfig: MainConfig = MainConfig(this)
    @Getter private val elementConfig: ElementConfig = ElementConfig(this)

    /* Settings */
    @Getter private val settings: MutableList<Setting<*, *>> = ArrayList()

    override fun generateJson(): BetterJsonObject {
        val json = BetterJsonObject()

        for (s in settings) {
            if (!s.shouldSave()) continue
            if (s is BooleanSettingWrapped) {
                // no need to save the default value
                if (s.getDefault() == s.get()) continue
                json.addProperty(s.getJsonKey(), s.get())
            } else if (s is IntSettingWrapped) {
                // no need to save the default value
                if (s.getDefault() == s.get()) continue
                json.addProperty(s.getJsonKey(), s.get())
            } else if (s is FloatSettingWrapped) {
                // no need to save the default value
                if (s.getDefault() == s.get()) continue
                json.addProperty(s.getJsonKey(), s.get())
            } else if (s is StringListSettingWrapped) {
                // no need to save the default value
                if (s.get() == s.getDefault()) continue
                json.addProperty(s.getJsonKey(), s.get())
            } else if (s is StringSettingWrapped) {
                // no need to save the default value
                if (s.get().equals(s.getDefault(), false)) continue
                json.addProperty(s.getJsonKey(), s.get())
            } // else if (s is EnumSetting) {
//                val setting: EnumSetting<*> = s as EnumSetting<*>
//                // no need to save the default value
//                if (setting.getIndex() === setting.getDefaultIndex()) continue
//                custom.addProperty(setting.getJsonKey(), setting.getIndex())
//            }
        }

        return json
    }

    override fun processJson(json: BetterJsonObject) {

    }

}