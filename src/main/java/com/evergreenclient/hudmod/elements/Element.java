/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements;

import com.evergreenclient.hudmod.elements.config.ElementConfig;
import com.evergreenclient.hudmod.utils.Position;
import com.evergreenclient.hudmod.utils.element.ElementData;
import com.evergreenclient.hudmod.utils.gui.Hitbox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.Color;

public abstract class Element extends Gui {

    /* For child classes */
    protected static final Minecraft mc = Minecraft.getMinecraft();

    /* Config */
    private boolean enabled;
    private Position pos;
    private boolean prefix = true;
    private boolean brackets = false;
    private boolean shadow = true;
    private Alignment alignment = Alignment.RIGHT;

    /* Color */
    private Color textColor = new Color(255, 255, 255, 255);
    private Color bgColor = new Color(0, 0, 0, 100);

    private final ElementConfig config;

    public Element() {
        pos = new Position(10, 10, 1);
        config = new ElementConfig(this);
        initialise();
        config.load();
    }

    public abstract void initialise();

    public abstract ElementData getMetadata();

    protected abstract String getValue();

    public abstract String getDisplayPrefix();

    public String getDisplayString() {
        String builder = "";
        if (showBrackets())
            builder += "[";
        if (showPrefix())
            builder += getDisplayPrefix() + ": ";
        builder += getValue();
        if (showBrackets())
            builder += "]";
        return builder;
    }

    /**
     * This can be overwritten if element has a very specific way of displaying itself
     */
    public void render() {
        GlStateManager.pushMatrix();
        GlStateManager.scale(getPosition().scale, getPosition().scale, 0);
        Hitbox hitbox = getHitbox();
        drawRect(hitbox.x, hitbox.y, hitbox.x + hitbox.width, hitbox.y + hitbox.height, getBgColor().getRGB());
        switch (getAlignment()) {
            case LEFT:
                mc.fontRendererObj.drawString(getDisplayString(), (getPosition().x - mc.fontRendererObj.getStringWidth(getDisplayString())) / getPosition().scale, getPosition().y / getPosition().scale, getTextColor().getRGB(), renderShadow());
                break;
            case CENTER:
                drawCenteredString(mc.fontRendererObj, getDisplayString(), getPosition().x / getPosition().scale, getPosition().y / getPosition().scale, getTextColor().getRGB(), renderShadow());
                break;
            case RIGHT:
                mc.fontRendererObj.drawString(getDisplayString(), getPosition().x / getPosition().scale, getPosition().y / getPosition().scale, getTextColor().getRGB(), renderShadow());
                break;
        }
        GlStateManager.popMatrix();
    }

    public void drawCenteredString(FontRenderer fontRendererIn, String text, float x, float y, int color, boolean shadow) {
        fontRendererIn.drawString(text, x - fontRendererIn.getStringWidth(text) / 2f, y, color, shadow);
    }

    public Hitbox getHitbox() {
        Hitbox hitbox = null;
        int width = mc.fontRendererObj.getStringWidth(getDisplayString());
        switch (getAlignment()) {
            case LEFT:
                hitbox = new Hitbox((int)(getPosition().x - width - 4 / getPosition().scale), (int)(pos.y - 4 / getPosition().scale), (int)(width + 8 / getPosition().scale), (int) (mc.fontRendererObj.FONT_HEIGHT + 8 / getPosition().scale));
                break;
            case CENTER:
                hitbox = new Hitbox((int)(pos.x - (width / 2) - 4 / getPosition().scale), (int)(pos.y - 4 / getPosition().scale), (int)(width + 8 / getPosition().scale), (int)(mc.fontRendererObj.FONT_HEIGHT + 8 / getPosition().scale));
                break;
            case RIGHT:
                hitbox = new Hitbox((int)(pos.x - 4 / getPosition().scale), (int)(pos.y - 4 / getPosition().scale), (int)(width + 8 / getPosition().scale), (int)(mc.fontRendererObj.FONT_HEIGHT + 8 / getPosition().scale));
                break;
        }
        return hitbox;
    }

    public void resetSettings() {
        enabled = true;
        pos = new Position(10, 10, 1);
        prefix = true;
        brackets = false;
        shadow = true;
        alignment = Alignment.RIGHT;
        textColor = new Color(255, 255, 255, 255);
        bgColor = new Color(0, 0, 0, 100);
        getConfig().save();
    }

    public ElementConfig getConfig() {
        return config;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Position getPosition() {
        return pos;
    }

    public boolean showPrefix() {
        return prefix;
    }

    public boolean showBrackets() {
        return brackets;
    }

    public Color getTextColor() {
        return textColor;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public boolean renderShadow() {
        return shadow;
    }

    public void setPrefix(boolean prefix) {
        this.prefix = prefix;
    }

    public void setBrackets(boolean brackets) {
        this.brackets = brackets;
    }

    public void setTextColor(Color color) {
        this.textColor = color;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public enum Alignment {
        LEFT("Left"),
        CENTER("Center"),
        RIGHT("Right");

        private String name;

        Alignment(String displayName) {
            this.name = displayName;
        }

        public String getName() {
            return name;
        }
    }
}
