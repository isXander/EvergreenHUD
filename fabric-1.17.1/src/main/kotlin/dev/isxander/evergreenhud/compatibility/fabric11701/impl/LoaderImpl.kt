package dev.isxander.evergreenhud.compatibility.fabric11701.impl

import dev.isxander.evergreenhud.compatibility.universal.impl.AILoader
import net.fabricmc.loader.api.FabricLoader

class LoaderImpl : AILoader() {
    override fun isModLoaded(id: String): Boolean =
        FabricLoader.getInstance().isModLoaded(id)
}