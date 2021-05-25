/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/Evergreen-Client/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;
import net.minecraft.client.Minecraft;

public class ElementFps extends SimpleTextElement {

    @Override
    public ElementData metadata() {
        return new ElementData("FPS Display", "Shows your current FPS", "Simple");
    }

    @Override
    protected String getValue() {
        return Integer.toString(Minecraft.getDebugFPS());
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "FPS";
    }

}
