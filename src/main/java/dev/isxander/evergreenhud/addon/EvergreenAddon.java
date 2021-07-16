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

package dev.isxander.evergreenhud.addon;

import dev.isxander.evergreenhud.EvergreenHUD;
import dev.isxander.evergreenhud.elements.ElementManager;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public abstract class EvergreenAddon {

    /**
     * Initialise elements here
     *
     * @see ElementManager#registerElement(String, Class)
     */
    public void init() {

    }

    /**
     * Once EvergreenHUD has been fully loaded
     *
     * @see EvergreenHUD#postInit(FMLPostInitializationEvent)
     */
    public void postInit() {

    }

    /**
     * Called when the config is loaded
     */
    public void configLoad() {

    }

    /**
     * Information about the addon.
     */
    public abstract AddonMeta metadata();

}
