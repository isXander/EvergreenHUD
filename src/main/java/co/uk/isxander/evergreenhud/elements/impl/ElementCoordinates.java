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
import co.uk.isxander.evergreenhud.settings.impl.ArraySetting;
import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.xanderlib.utils.GuiUtils;
import co.uk.isxander.xanderlib.utils.HitBox2D;
import co.uk.isxander.xanderlib.utils.Resolution;
import net.apolloclient.utils.GLRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ElementCoordinates extends Element {

    public BooleanSetting showX;
    public BooleanSetting showY;
    public BooleanSetting showZ;
    public BooleanSetting showCoord;
    public IntegerSetting accuracy;
    public BooleanSetting trailingZeros;
    public ArraySetting type;
    public IntegerSetting verticalSpacing;

    @Override
    public void initialise() {
        addSettings(showCoord = new BooleanSetting("Show Name", "Display", "Show X: Y: and Z: before the values.", true));
        addSettings(type = new ArraySetting("Display Type", "Display", "How the coordinates are displayed.", "Vertical", new String[]{"Vertical", "Horizontal"}));
        addSettings(verticalSpacing = new IntegerSetting("Vertical Spacing", "Display", "How far apart each line will be.", 2, 0, 5, "") {
            @Override
            public boolean isDisabled() {
                return type.getIndex() != 0;
            }
        });
        addSettings(showX = new BooleanSetting("Show X", "Display", "Show the X coordinate.", true));
        addSettings(showY = new BooleanSetting("Show Y", "Display", "Show the Y coordinate.", true));
        addSettings(showZ = new BooleanSetting("Show Z", "Display", "Show the Z coordinate.", true));
        addSettings(accuracy = new IntegerSetting("Accuracy", "Functionality", "How many decimal places the value should display.", 0, 0, 4, " places"));
        addSettings(trailingZeros = new BooleanSetting("Trailing Zeros", "Functionality", "Add zeroes to match the accuracy.", false));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("Coordinates", "Shows your current coordinates.");
    }

    @Override
    public void render(RenderGameOverlayEvent event) {
        mc.mcProfiler.startSection(getMetadata().getName());

        float scale = getPosition().getScale();
        boolean chroma = getChromaSetting().get();
        TextMode textMode = getTextModeSetting().get();
        int color = getTextColor().getRGB();

        HitBox2D hitbox = calculateHitbox(1, scale);
        float x = getPosition().getRawX(event.resolution);
        float y = getPosition().getRawY(event.resolution);
        GLRenderer.drawRectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height, getBgColor());
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 0);

        int i = 0;
        for (String line : getMultiValue()) {
            float posY = ((y / getPosition().getScale()) + (mc.fontRendererObj.FONT_HEIGHT * i) + (verticalSpacing.get() * i));
            switch (getAlignmentSetting().get()) {
                case RIGHT:
                    float posX = (x - mc.fontRendererObj.getStringWidth(line)) / scale;

                    GuiUtils.drawString(mc.fontRendererObj, line, posX, posY, textMode == TextMode.SHADOW, textMode == TextMode.BORDER, chroma, false, color);

                    break;
                case CENTER:
                    posX = x / scale;

                    GuiUtils.drawString(mc.fontRendererObj, line, posX, posY, textMode == TextMode.SHADOW, textMode == TextMode.BORDER, chroma, true, color);

                    break;
                case LEFT:
                    posX = x / scale;

                    GuiUtils.drawString(mc.fontRendererObj, line, posX, posY, textMode == TextMode.SHADOW, textMode == TextMode.BORDER, chroma, false, color);

                    break;
            }

            i++;
        }

        GlStateManager.popMatrix();
        mc.mcProfiler.endSection();
    }

    @Override
    protected String getValue() {
        return null;
    }

    protected List<String> getMultiValue() {
        List<String> lines = new ArrayList<>();
        if (mc.thePlayer == null) {
            lines.add("Unknown");
            return lines;
        }
        String formatter = (trailingZeros.get() ? "0" : "#");
        StringBuilder sb = new StringBuilder(accuracy.get() < 1 ? formatter : formatter + ".");
        for (int i = 0; i < accuracy.get(); i++) sb.append(formatter);
        DecimalFormat df = new DecimalFormat(sb.toString());
        if (type.get().equalsIgnoreCase("vertical")) {
            if (showX.get()) lines.add((getBracketsSetting().get() ? "[" : "") + (showCoord.get() ? "X: " : "") + df.format(mc.thePlayer.posX) + (getBracketsSetting().get() ? "]" : ""));
            if (showY.get()) lines.add((getBracketsSetting().get() ? "[" : "") + (showCoord.get() ? "Y: " : "") + df.format(mc.thePlayer.posY) + (getBracketsSetting().get() ? "]" : ""));
            if (showZ.get()) lines.add((getBracketsSetting().get() ? "[" : "") + (showCoord.get() ? "Z: " : "") + df.format(mc.thePlayer.posZ) + (getBracketsSetting().get() ? "]" : ""));
        } else {
            String builder = "";
            if (showX.get()) builder += (showCoord.get() ? "X: " : "") + df.format(mc.thePlayer.posX) + (showY.get() || showZ.get() ? ", " : "");
            if (showY.get()) builder += (showCoord.get() ? "Y: " : "") + df.format(mc.thePlayer.posY) + (showZ.get() ? ", " : "");
            if (showZ.get()) builder += (showCoord.get() ? "Z: " : "") + df.format(mc.thePlayer.posZ);

            if (getInvertTitleSetting().get()) builder = getDefaultDisplayTitle() + " " + builder;
            else builder = builder + " " + getDefaultDisplayTitle();

            if (getBracketsSetting().get()) builder = "[" + builder + "]";
            lines.add(builder);
        }
        return lines;
    }

    @Override
    public HitBox2D calculateHitbox(float gl, float sizeScale) {
        HitBox2D hitbox = null;
        ScaledResolution res = Resolution.get();
        List<String> value = getMultiValue();

        float width = 10;
        for (String line : value) {
            width = Math.max(width, mc.fontRendererObj.getStringWidth(line));
        }
        width = Math.max(10, width);
        width *= sizeScale;

        float extraWidth = getPaddingWidthSetting().get() * sizeScale;
        float height = ((mc.fontRendererObj.FONT_HEIGHT * value.size()) + (verticalSpacing.get() * (value.size() - 1))) * sizeScale;
        float extraHeight = getPaddingHeightSetting().get() * sizeScale;
        float x = getPosition().getRawX(res) / gl;
        float y = getPosition().getRawY(res) / gl;
        switch (getAlignmentSetting().get()) {
            case RIGHT:
                hitbox = new HitBox2D(x - (width / sizeScale) - extraWidth, y - extraHeight, width + (extraWidth * 2), height + (extraHeight * 2));
                break;
            case CENTER:
                hitbox = new HitBox2D(x - (width / 2f) - extraWidth, y - extraHeight, width + (extraWidth * 2), height + (extraHeight * 2));
                break;
            case LEFT:
                hitbox = new HitBox2D(x - extraWidth, y - extraHeight, width + (extraWidth * 2), height + (extraHeight * 2));
                break;
        }
        return hitbox;
    }

    @Override
    public boolean useInvertedSetting() {
        return type.get().equalsIgnoreCase("horizontal");
    }

    @Override
    public boolean useTitleSetting() {
        return type.get().equalsIgnoreCase("horizontal");
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Coords";
    }

}
