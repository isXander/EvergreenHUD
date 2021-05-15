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
import co.uk.isxander.evergreenhud.settings.impl.EnumSetting;
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
    public EnumSetting<SpeedUnit> speedUnit;
    public BooleanSetting useX;
    public BooleanSetting useY;
    public BooleanSetting useZ;

    @Override
    public void initialise() {
        addSettings(speedUnit = new EnumSetting<>("Unit", "Functionality", "What unit of speed to display.", SpeedUnit.METERS_PER_SEC));
        addSettings(accuracy = new IntegerSetting("Accuracy", "Display", "How many decimal places the value should display.", 2, 0, 4, " places"));
        addSettings(trailingZeros = new BooleanSetting("Trailing Zeros", "Display", "Add zeroes to match the accuracy.", false));
        addSettings(suffix = new BooleanSetting("Suffix", "Display", "If the value should be suffixed with the specified unit.", false));
        addSettings(useX = new BooleanSetting("Use X", "Functionality", "Use the x coordinate when calculating the speed.", true));
        addSettings(useY = new BooleanSetting("Use Y", "Functionality", "Use the y coordinate when calculating the speed.", true));
        addSettings(useZ = new BooleanSetting("Use Z", "Functionality", "Use the z coordinate when calculating the speed.", true));
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
        return new DecimalFormat(sb.toString()).format(speed) + (suffix.get() ? " " + speedUnit.get().display : "");
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Speed";
    }

    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        double distTraveledLastTickX = (useX.get() ? mc.thePlayer.posX - mc.thePlayer.prevPosX : 0);
        double distTraveledLastTickY = (useY.get() ? mc.thePlayer.posY - mc.thePlayer.prevPosY : 0);
        double distTraveledLastTickZ = (useZ.get() ? mc.thePlayer.posZ - mc.thePlayer.prevPosZ : 0);

        this.speed = convertSpeed(MathHelper.sqrt_double(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickY * distTraveledLastTickY + distTraveledLastTickZ * distTraveledLastTickZ) * 20);
    }

    private double convertSpeed(double speed) {
        switch (speedUnit.get()) {
            case MPH:
                speed *= 2.237;
                break;
            case KPH:
                speed *= 3.6;
                break;
        }
        return speed;
    }

    private enum SpeedUnit {
        METERS_PER_SEC("m/s"),
        MPH("mph"),
        KPH("kph");

        public final String display;
        SpeedUnit(String display) {
            this.display = display;
        }
    }

}
