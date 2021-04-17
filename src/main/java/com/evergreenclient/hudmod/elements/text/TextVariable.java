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

package com.evergreenclient.hudmod.elements.text;

import net.apolloclient.utils.GLRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public abstract class TextVariable extends Gui {

    private static Minecraft mc = Minecraft.getMinecraft();

    private final String key;

    private boolean mouseClicked;
    private float x;
    private float y;

    public TextVariable(String key) {
        this.key = key;
        this.mouseClicked = false;
    }

    public String getKey() {
        return this.key;
    }

    public abstract String getValue();

    @Override
    public String toString() {
        return getValue();
    }

    public void render(float x, float y, float scale, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        GlStateManager.translate(-scale, -scale, 0);
        GLRenderer.drawRoundedRectangle((int)(x - (x / 2f)), (int) y, 120, 40, 15, color);
        mc.fontRendererObj.drawString(key, x - (x / 2f), y + 20 - (mc.fontRendererObj.FONT_HEIGHT / 2f), color.getRGB(), true);
        GlStateManager.popMatrix();
    }



}
