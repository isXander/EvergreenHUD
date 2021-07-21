/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/gpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.config.profile

import dev.isxander.evergreenhud.EvergreenHUD
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIImage
import java.io.File
import java.net.URL

open class DefaultIconProvider {
    open val path: String = File(EvergreenHUD.RESOURCE_DIR, "icon.png").path

    // cannot make this an abstract property
    // because GSON won't allow @Expose annotation
    @Transient
    open val iconComponent: UIComponent = UIImage.ofFile(File(EvergreenHUD.RESOURCE_DIR, "icon.png"))

}

class FileIconProvider(@Transient val file: File) : DefaultIconProvider() {
    override val path: String = file.path

    override val iconComponent: UIComponent
        get() = UIImage.ofFile(File(path))
}

class UrlIconProvider(override val path: String) : DefaultIconProvider() {
    override val iconComponent: UIComponent
        get() = UIImage.ofURL(URL(path))
}