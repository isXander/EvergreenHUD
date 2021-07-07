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

import co.uk.isxander.evergreenhud.elements.type.MultiLineTextElement;
import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.evergreenhud.settings.impl.EnumSetting;
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.xanderlib.utils.Facing;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ElementCoordinates extends MultiLineTextElement {

    public EnumSetting<DisplayMode> mode;
    public BooleanSetting showX;
    public BooleanSetting showY;
    public BooleanSetting showZ;
    public BooleanSetting showCoord;
    public BooleanSetting showDirection;
    public IntegerSetting accuracy;
    public BooleanSetting trailingZeros;

    @Override
    public void initialise() {
        addSettings(mode = new EnumSetting<>("Mode", "Display", "How the coordinates should be displayed.", DisplayMode.VERTICAL));
        addSettings(showCoord = new BooleanSetting("Show Name", "Display", "Show X: Y: and Z: before the values.", true));
        addSettings(showDirection = new BooleanSetting("Show Direction", "Display", "Show if the axis is going to increase or decrease based on your direction.", false));
        addSettings(showX = new BooleanSetting("Show X", "Display", "Show the X coordinate.", true));
        addSettings(showY = new BooleanSetting("Show Y", "Display", "Show the Y coordinate.", true));
        addSettings(showZ = new BooleanSetting("Show Z", "Display", "Show the Z coordinate.", true));
        addSettings(accuracy = new IntegerSetting("Accuracy", "Functionality", "How many decimal places the value should display.", 0, 0, 4, " places"));
        addSettings(trailingZeros = new BooleanSetting("Trailing Zeros", "Functionality", "Add zeroes to match the accuracy.", false));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("Coordinates", "Shows your current coordinates.", "Advanced");
    }

    @Override
    protected List<String> getValue() {
        List<String> lines = new ArrayList<>();
        if (mc.thePlayer == null) {
            lines.add("Unknown");
            return lines;
        }
        String formatter = (trailingZeros.get() ? "0" : "#");
        StringBuilder formatBuilder = new StringBuilder(accuracy.get() < 1 ? formatter : formatter + ".");
        for (int i = 0; i < accuracy.get(); i++) formatBuilder.append(formatter);
        DecimalFormat df = new DecimalFormat(formatBuilder.toString());

        StringBuilder sb = new StringBuilder();
        Facing facing = Facing.parse(mc.thePlayer.rotationYaw);
        if (showX.get()) {
            sb.append(showCoord.get() ? "X: " : "");
            sb.append(df.format(mc.thePlayer.posX));
            if (showDirection.get()) {
                sb.append(" (");
                if (facing == Facing.EAST || facing == Facing.NORTH_EAST || facing == Facing.SOUTH_EAST)
                    sb.append("+");
                else if (facing == Facing.WEST || facing == Facing.NORTH_WEST || facing == Facing.SOUTH_WEST)
                    sb.append("-");
                else
                    sb.append(" ");
                sb.append(")");
            }

            if (mode.get() == DisplayMode.VERTICAL) {
                lines.add(sb.toString());
                sb.setLength(0);
            } else if (showY.get() || showZ.get()) {
                sb.append(", ");
            }
        }
        if (showY.get()) {
            sb.append(showCoord.get() ? "Y: " : "");
            sb.append(df.format(mc.thePlayer.posY));

            if (mode.get() == DisplayMode.VERTICAL) {
                lines.add(sb.toString());
                sb.setLength(0);
            } else if (showZ.get()) {
                sb.append(", ");
            }
        }
        if (showZ.get()) {
            sb.append(showCoord.get() ? "Z: " : "");
            sb.append(df.format(mc.thePlayer.posZ));
            if (showDirection.get()) {
                sb.append(" (");
                if (facing == Facing.NORTH || facing == Facing.NORTH_EAST || facing == Facing.NORTH_WEST)
                    sb.append("-");
                else if (facing == Facing.SOUTH || facing == Facing.SOUTH_WEST || facing == Facing.SOUTH_EAST)
                    sb.append("+");
                else
                    sb.append(" ");
                sb.append(")");
            }

            if (mode.get() == DisplayMode.VERTICAL) {
                lines.add(sb.toString());
                sb.setLength(0);
            }
        }

        if (mode.get() == DisplayMode.HORIZONTAL) {
            lines.add(sb.toString());
        }
        return lines;
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Coords";
    }

    public enum DisplayMode {
        VERTICAL,
        HORIZONTAL
    }

}
