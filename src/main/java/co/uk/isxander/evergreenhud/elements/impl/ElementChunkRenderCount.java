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

package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.elements.ElementData;

public class ElementChunkRenderCount extends Element {

    @Override
    public void initialise() {

    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Chunk Counter", "Displays how many chunks are currently being rendered.");
    }

    @Override
    protected String getValue() {
        if (mc.renderGlobal == null)
            return "Unknown";

        return mc.renderGlobal.getDebugInfoRenders().split("/")[0].substring(3);
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Chunks";
    }

}
