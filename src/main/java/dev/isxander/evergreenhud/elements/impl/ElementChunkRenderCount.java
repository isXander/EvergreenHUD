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

package dev.isxander.evergreenhud.elements.impl;

import dev.isxander.evergreenhud.elements.ElementData;
import dev.isxander.evergreenhud.elements.type.SimpleTextElement;
import dev.isxander.evergreenhud.mixins.AccessorRenderGlobal;

public class ElementChunkRenderCount extends SimpleTextElement {

    @Override
    protected ElementData metadata() {
        return new ElementData("Chunk Counter", "Displays how many chunks are currently being rendered.", "Simple");
    }

    @Override
    protected String getValue() {
        if (mc.renderGlobal == null || ((AccessorRenderGlobal) mc.renderGlobal).getViewFrustum() == null)
            return "Unknown";

        return mc.renderGlobal.getDebugInfoRenders().split("/")[0].substring(3);
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Chunks";
    }

}
