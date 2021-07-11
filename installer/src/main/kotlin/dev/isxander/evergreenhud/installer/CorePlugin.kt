package dev.isxander.evergreenhud.installer

import co.uk.isxander.libinstaller.InstallerUtils
import co.uk.isxander.libinstaller.ModCoreInstaller
import co.uk.isxander.libinstaller.XanderLibInstaller
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin

class CorePlugin : IFMLLoadingPlugin {

    override fun getASMTransformerClass(): Array<String> {
        println(InstallerUtils.fetchJSON("https://api.sk1er.club/modcore_versions"))

        val initialize = ModCoreInstaller.initialise(Launch.minecraftHome)

        if (ModCoreInstaller.isErrored() || initialize != 0 && initialize != -1) {
            println("Failed to load Sk1er Modcore - " + initialize + " - " + ModCoreInstaller.getError())
        }
        println("Installing XanderLib....")
        XanderLibInstaller.initialise(Launch.minecraftHome)
        println("Installing EvergreenHUD")
        EvergreenHUDInstaller.install(Launch.minecraftHome)

        // If true the classes are loaded
        return if (ModCoreInstaller.isInitialised()) arrayOf("club.sk1er.mods.core.forge.ClassTransformer")
        else arrayOf()

    }

    override fun getModContainerClass() = null

    override fun getSetupClass() = null

    override fun injectData(data: MutableMap<String, Any>?) {
    }

    override fun getAccessTransformerClass() = null


}