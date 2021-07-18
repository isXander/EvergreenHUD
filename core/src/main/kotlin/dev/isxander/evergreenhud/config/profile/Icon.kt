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