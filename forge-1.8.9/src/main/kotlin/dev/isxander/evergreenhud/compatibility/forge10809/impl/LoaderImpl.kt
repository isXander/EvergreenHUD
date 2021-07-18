package dev.isxander.evergreenhud.compatibility.forge10809.impl

import dev.isxander.evergreenhud.compatibility.universal.impl.AILoader
import net.minecraftforge.fml.common.Loader

class LoaderImpl : AILoader() {
    override fun isModLoaded(id: String): Boolean = Loader.isModLoaded(id)
}