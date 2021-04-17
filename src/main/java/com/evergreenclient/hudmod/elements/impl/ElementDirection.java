/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.elements.ElementType;
import com.evergreenclient.hudmod.settings.impl.BooleanSetting;
import com.evergreenclient.hudmod.utils.Facing;
import com.evergreenclient.hudmod.utils.ElementData;
import net.minecraft.util.MathHelper;

public class ElementDirection extends Element {

    private BooleanSetting abbreviated;

    @Override
    public void initialise() {
        addSettings(abbreviated = new BooleanSetting("Abbreviated", "If the direction should be abbreviated to \"N\" from \"North\"", true));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("Direction", "Shows what direction you are facing.");
    }

    @Override
    protected String getValue() {
        float rotationYaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
        Facing facing = Facing.NORTH;
        if (rotationYaw >= 165f || rotationYaw <= -165f) {
            facing = Facing.NORTH;
        } else if (rotationYaw >= -165f && rotationYaw <= -105f) {
            facing = Facing.NORTH_EAST;
        } else if (rotationYaw >= -105f && rotationYaw <= -75f) {
            facing = Facing.EAST;
        } else if (rotationYaw >= -75f && rotationYaw <= -15f) {
            facing = Facing.SOUTH_EAST;
        } else if (rotationYaw >= -15f && rotationYaw <= 15f) {
            facing = Facing.SOUTH;
        } else if (rotationYaw >= 15f && rotationYaw <= 75f) {
            facing = Facing.SOUTH_WEST;
        } else if (rotationYaw >= 75f && rotationYaw <= 105f) {
            facing = Facing.WEST;
        } else if (rotationYaw >= 105f && rotationYaw <= 165f) {
            facing = Facing.NORTH_WEST;
        }
        if (abbreviated.get())
            return facing.getAbbreviated();
        return facing.getNormal();
    }

    @Override
    public String getDisplayTitle() {
        return "Direction";
    }

}
