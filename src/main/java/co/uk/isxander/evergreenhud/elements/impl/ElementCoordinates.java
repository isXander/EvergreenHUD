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

import co.uk.isxander.evergreenhud.elements.type.MultiLineTextElement;
import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.evergreenhud.elements.ElementData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ElementCoordinates extends MultiLineTextElement {

    public BooleanSetting showX;
    public BooleanSetting showY;
    public BooleanSetting showZ;
    public BooleanSetting showCoord;
    public IntegerSetting accuracy;
    public BooleanSetting trailingZeros;

    @Override
    public void initialise() {
        addSettings(showCoord = new BooleanSetting("Show Name", "Display", "Show X: Y: and Z: before the values.", true));
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
        StringBuilder sb = new StringBuilder(accuracy.get() < 1 ? formatter : formatter + ".");
        for (int i = 0; i < accuracy.get(); i++) sb.append(formatter);
        DecimalFormat df = new DecimalFormat(sb.toString());
        if (showX.get()) lines.add((showCoord.get() ? "X: " : "") + df.format(mc.thePlayer.posX));
        if (showY.get()) lines.add((showCoord.get() ? "Y: " : "") + df.format(mc.thePlayer.posY));
        if (showZ.get()) lines.add((showCoord.get() ? "Z: " : "") + df.format(mc.thePlayer.posZ));
        return lines;
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Coords";
    }

}
