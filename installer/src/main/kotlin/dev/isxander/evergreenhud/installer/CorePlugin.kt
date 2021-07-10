package dev.isxander.evergreenhud.installer

import net.minecraft.launchwrapper.Launch
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin

class CorePlugin : IFMLLoadingPlugin {

    override fun getASMTransformerClass(): Array<String> {
        val initialize = ModCoreInstaller.initialise(Launch.minecraftHome)

        if (ModCoreInstaller.isErrored() || initialize != 0 && initialize != -1) {
            println("Failed to load Sk1er Modcore - " + initialize + " - " + ModCoreInstaller.getError())
        }
        // If true the classes are loaded
        return if (ModCoreInstaller.isInitialised()) {
            // ModCore has been successfully installed. Now initialise XanderLib
            XanderLibInstaller.initialise(Launch.minecraftHome)

            // register ModCore's class transformer
            arrayOf("club.sk1er.mods.core.forge.ClassTransformer")
        } else arrayOf()

    }

    override fun getModContainerClass() = null

    override fun getSetupClass() = null

    override fun injectData(data: MutableMap<String, Any>?) {
    }

    override fun getAccessTransformerClass() = null


}