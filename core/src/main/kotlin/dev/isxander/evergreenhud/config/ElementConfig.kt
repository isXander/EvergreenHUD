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

package dev.isxander.evergreenhud.config

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.elements.ElementManager
import java.io.File

class ElementConfig(element: ElementManager) {



    companion object {
        const val SCHEMA = 4
        val ELEMENTS_FILE = File(EvergreenHUD.DATA_DIR, "elements.json")
    }

}