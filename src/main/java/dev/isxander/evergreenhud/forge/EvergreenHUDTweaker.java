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

package dev.isxander.evergreenhud.forge;

import gg.essential.loader.stage0.EssentialSetupTweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinTweaker;

import java.io.File;
import java.util.List;

public class EvergreenHUDTweaker extends EssentialSetupTweaker {

    private final MixinTweaker mixinTweaker;
    public EvergreenHUDTweaker() {
        mixinTweaker = new MixinTweaker();
    }

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        super.acceptOptions(args, gameDir, assetsDir, profile);
        mixinTweaker.acceptOptions(args, gameDir, assetsDir, profile);
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        super.injectIntoClassLoader(classLoader);
        mixinTweaker.injectIntoClassLoader(classLoader);
    }

    @Override
    public String getLaunchTarget() {
        return mixinTweaker.getLaunchTarget();
    }

}
