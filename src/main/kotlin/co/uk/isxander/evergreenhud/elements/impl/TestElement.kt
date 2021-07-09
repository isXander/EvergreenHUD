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

package co.uk.isxander.evergreenhud.elements.impl

import co.uk.isxander.evergreenhud.elements.Element
import co.uk.isxander.evergreenhud.elements.ElementMeta
import co.uk.isxander.evergreenhud.settings.BooleanSetting

@ElementMeta(name = "Test Element", category = "Test Category", description = "A test element description very descriptive.")
class TestElement : Element() {

    @BooleanSetting(name = "Test Bool setting", category = "pog category", description = "descriptionnnnn")
    var setting: Boolean = true

}