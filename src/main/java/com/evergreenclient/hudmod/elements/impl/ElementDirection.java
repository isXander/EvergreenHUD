/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.settings.impl.BooleanSetting;
import com.evergreenclient.hudmod.utils.StringUtils;
import com.evergreenclient.hudmod.utils.element.ElementData;
import net.minecraft.util.EnumFacing;

public class ElementDirection extends Element {

    private BooleanSetting abbreviated;

    @Override
    public void initialise() {
        addSettings(abbreviated = new BooleanSetting("Abbreviated", true));
    }

    @Override
    public ElementData getMetadata() {
        return new ElementData("Direction", "Shows what direction you are facing.");
    }

    @Override
    protected String getValue() {
        EnumFacing facing = mc.thePlayer.getHorizontalFacing();
        String value = facing.getName();
        if (abbreviated.get())
            value = value.substring(0, 1);
        return StringUtils.firstUpper(value);
    }

    @Override
    public String getDisplayTitle() {
        return "Direction";
    }

}
