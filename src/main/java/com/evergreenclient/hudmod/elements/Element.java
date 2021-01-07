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
import java.util.ArrayList;
import java.util.List;

public abstract class Element extends Gui {

    /* For child classes */
    protected static final Minecraft mc = Minecraft.getMinecraft();

    /* Config */
    private boolean enabled;
    private final Position pos;
    private boolean prefix = true;
    private boolean brackets = false;
    private boolean shadow = true;
    private boolean centered = false;
    private final List<ElementConfig.ParsableObject> customObjects;

    /* Color */
    private Color textColor = new Color(255, 255, 255, 255);
    private Color bgColor = new Color(0, 0, 0, 100);

    private final ElementConfig config;

    public Element() {
        pos = new Position(10, 10, 1);
        customObjects = new ArrayList<>();
        config = new ElementConfig(this);
        initialise();
        config.load();
    }

    public abstract void initialise();

    public abstract ElementData getMetadata();

    protected abstract String getValue();

    public abstract String getDisplayPrefix();

    private String getDisplayString() {
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
        if (isCentered())
            drawCenteredString(mc.fontRendererObj, getDisplayString(), getPosition().x / getPosition().scale, getPosition().y / getPosition().scale, getTextColor().getRGB(), renderShadow());
        else
            mc.fontRendererObj.drawString(getDisplayString(), getPosition().x / getPosition().scale, getPosition().y / getPosition().scale, getTextColor().getRGB(), renderShadow());
        GlStateManager.popMatrix();
    }

    public void drawCenteredString(FontRenderer fontRendererIn, String text, float x, float y, int color, boolean shadow) {
        fontRendererIn.drawString(text, x - fontRendererIn.getStringWidth(text) / 2f, y, color, shadow);
    }

    public Hitbox getHitbox() {
        int width = mc.fontRendererObj.getStringWidth(getDisplayString());
        return new Hitbox(pos.x - (isCentered() ? (width / 2) : 0) - 4, pos.y - 4, (int) (width + 8 / getPosition().scale), (int) (mc.fontRendererObj.FONT_HEIGHT + 8 / getPosition().scale));
    }

    protected void addSettingObject(String key, Object o) {
        customObjects.add(new ElementConfig.ParsableObject(key, o));
    }

    @SuppressWarnings("unchecked")
    public <T> T getSettingObject(String key) {
        return (T)customObjects.stream().filter(po -> po.key.equals(key)).findAny().orElse(null);
    }

    public List<ElementConfig.ParsableObject> getCustomObjects() {
        return customObjects;
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

    public boolean isCentered() {
        return centered;
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
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
}
