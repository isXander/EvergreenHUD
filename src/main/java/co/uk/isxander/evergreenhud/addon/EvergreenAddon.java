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

package co.uk.isxander.evergreenhud.addon;

import co.uk.isxander.evergreenhud.elements.ElementType;

public abstract class EvergreenAddon {

    /**
     * Initialise elements here
     *
     * @see ElementType#registerElement(String, Class)
     */
    public abstract void init();

    /**
     * Information about the addon.
     */
    public abstract AddonMeta metadata();

}
