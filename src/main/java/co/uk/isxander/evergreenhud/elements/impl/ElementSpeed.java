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

import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.evergreenhud.elements.ElementData;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.text.DecimalFormat;

public class ElementSpeed extends Element {

    private double speed = 0;

    public IntegerSetting accuracy;
    public BooleanSetting trailingZeros;
    public BooleanSetting suffix;

    @Override
    public void initialise() {
        addSettings(accuracy = new IntegerSetting("Accuracy", "How many decimal places the value should display.", 2, 0, 4, " places"));
        addSettings(trailingZeros = new BooleanSetting("Trailing Zeros", "Add zeroes to match the accuracy.", false));
        addSettings(suffix = new BooleanSetting("Suffix", "If the value should be suffixed with \"ms\"", false));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("Speed Display", "Displays the blocks per second speed of the player.");
    }

    @Override
    protected String getValue() {
        String format = (trailingZeros.get() ? "0" : "#");
        StringBuilder sb = new StringBuilder(accuracy.get() < 1 ? format : format + ".");
        for (int i = 0; i < accuracy.get(); i++) sb.append(format);
        return new DecimalFormat(sb.toString()).format(speed) + (suffix.get() ? " m/s" : "");
    }

    @Override
    public String getDisplayTitle() {
        return "Speed";
    }

    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        double distTraveledLastTickX = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double distTraveledLastTickY = mc.thePlayer.posY - mc.thePlayer.prevPosY;
        double distTraveledLastTickZ = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;

        this.speed = MathHelper.sqrt_double(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickY * distTraveledLastTickY + distTraveledLastTickZ * distTraveledLastTickZ) * 20;
    }

}
