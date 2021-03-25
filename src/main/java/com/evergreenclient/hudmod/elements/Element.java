/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements;

import com.evergreenclient.hudmod.EvergreenHUD;
import com.evergreenclient.hudmod.config.ElementConfig;
import com.evergreenclient.hudmod.event.EventManager;
import com.evergreenclient.hudmod.event.Listenable;
import com.evergreenclient.hudmod.gui.screens.impl.GuiElementConfig;
import com.evergreenclient.hudmod.settings.Setting;
import com.evergreenclient.hudmod.utils.Alignment;
import com.evergreenclient.hudmod.utils.Position;
import com.evergreenclient.hudmod.utils.element.ElementData;
import com.evergreenclient.hudmod.utils.gui.Hitbox;
import com.evergreenclient.hudmod.utils.thirdparty.GLRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Element extends Gui implements Listenable {

    @Override
    public boolean canReceiveEvents() {
        return isEnabled() && EvergreenHUD.getInstance().getElementManager().isEnabled();
    }

    /* For child classes */
    protected static final Minecraft mc = Minecraft.getMinecraft();

    /* Config */
    private boolean enabled = false;
    private Position pos = Position.getPositionWithRawPositioning(10, 10, 1, new ScaledResolution(mc));
    private boolean title = true;
    private boolean brackets = false;
    private boolean shadow = true;
    private boolean chroma = false;
    private boolean inverted = false;
    private Alignment alignment = Alignment.RIGHT;
    private final List<Setting> customSettings = new ArrayList<>();

    /* Color */
    private Color textColor = new Color(255, 255, 255);
    private Color bgColor = new Color(0, 0, 0, 100);

    protected final Logger logger;
    private final ElementConfig config;
    private final ElementData meta;

    public Element() {
        EventManager.getInstance().addListener(this);
        this.meta = metadata();
        this.logger = LogManager.getLogger(getMetadata().getName());
        config = new ElementConfig(this);
        initialise();
        config.load();
    }

    public abstract void initialise();

    // performance: avoid repeat initialization
    protected abstract ElementData metadata();
    public final ElementData getMetadata() {
        return meta;
    }

    protected abstract String getValue();

    public abstract String getDisplayTitle();

    public GuiElementConfig getElementConfigGui() {
        return new GuiElementConfig(this);
    }

    public boolean canShowTitle() {
        return true;
    }

    public String getDisplayString() {
        String builder = "";
        if (showBrackets())
            builder += "[";
        if (showTitle() && !isInverted() && canShowTitle())
            builder += getDisplayTitle() + ": ";
        builder += getValue();
        if (showTitle() && isInverted() && canShowTitle())
            builder += " " + getDisplayTitle();
        if (showBrackets())
            builder += "]";
        return builder;
    }

    /**
     * This can be overwritten if element has a very specific way of displaying itself
     */
    public void render(RenderGameOverlayEvent event) {
        mc.mcProfiler.startSection(getMetadata().getName());
        GlStateManager.pushMatrix();
        GlStateManager.scale(getPosition().getScale(), getPosition().getScale(), 0);
        Hitbox hitbox = getHitbox();
        float x = getPosition().getRawX(event.resolution);
        float y = getPosition().getRawY(event.resolution);
        drawRect((int) (hitbox.x / getPosition().getScale()), (int) (hitbox.y / getPosition().getScale()), (int) ((hitbox.x / getPosition().getScale()) + (hitbox.width)), (int) (hitbox.y / getPosition().getScale() + (hitbox.height)), getBgColor().getRGB());
        switch (getAlignment()) {
            case LEFT:
                float posX = (x - mc.fontRendererObj.getStringWidth(getDisplayString())) / getPosition().getScale();
                float posY = y / getPosition().getScale();

                if (chroma)
                    drawChromaString(getDisplayString(), posX, posY, renderShadow(), false);
                else
                    mc.fontRendererObj.drawString(getDisplayString(), posX, posY, getTextColor().getRGB(), renderShadow());
                break;
            case CENTER:
                posX = x / getPosition().getScale();
                posY = y / getPosition().getScale();

                if (chroma)
                    drawChromaString(getDisplayString(), posX, posY, renderShadow(), true);
                else
                    drawCenteredString(mc.fontRendererObj, getDisplayString(), posX, posY, getTextColor().getRGB(), renderShadow());
                break;
            case RIGHT:
                posX = x / getPosition().getScale();
                posY = y / getPosition().getScale();

                if (chroma)
                    drawChromaString(getDisplayString(), posX, posY, renderShadow(), false);
                else
                    mc.fontRendererObj.drawString(getDisplayString(), posX, posY, getTextColor().getRGB(), renderShadow());
                break;
        }
        GlStateManager.popMatrix();
        mc.mcProfiler.endSection();
    }

    public void drawCenteredString(FontRenderer fontRendererIn, String text, float x, float y, int color, boolean shadow) {
        fontRendererIn.drawString(text, x - fontRendererIn.getStringWidth(text) / 2f, y, color, shadow);
    }

    protected void drawChromaString(String text, float x, float y, boolean shadow, boolean centered) {
        if (centered)
            x -= mc.fontRendererObj.getStringWidth(text) / 2f;

        for (char c : text.toCharArray()) {
            int i = getChroma(x, y).getRGB();
            String tmp = String.valueOf(c);
            mc.fontRendererObj.drawString(tmp, x, y, i, shadow);
            x += mc.fontRendererObj.getStringWidth(tmp);
        }
    }

    private Color getChroma(double x, double y) {
        float v = 2000.0f;
        return new Color(Color.HSBtoRGB((float)((System.currentTimeMillis() - x * 10.0 * 1.0 - y * 10.0 * 1.0) % v) / v, 0.8f, 0.8f));
    }

    public Hitbox getHitbox() {
        Hitbox hitbox = null;
        ScaledResolution res = new ScaledResolution(mc);
        int width = mc.fontRendererObj.getStringWidth(getDisplayString());
        float x = getPosition().getRawX(res);
        float y = getPosition().getRawY(res);
        switch (getAlignment()) {
            case LEFT:
                hitbox = new Hitbox((x - width - 4), (y - 4), width + 8, mc.fontRendererObj.FONT_HEIGHT + 8);
                break;
            case CENTER:
                hitbox = new Hitbox((x - (width / 2f) - 4), (y - 4), width + 8, mc.fontRendererObj.FONT_HEIGHT + 8);
                break;
            case RIGHT:
                hitbox = new Hitbox((x - 4), (y - 4), width + 8, mc.fontRendererObj.FONT_HEIGHT + 8);
                break;
        }
        return hitbox;
    }

    public void resetSettings() {
        enabled = true;
        pos = Position.getPositionWithRawPositioning(10, 10, 1, new ScaledResolution(mc));
        title = true;
        brackets = false;
        inverted = false;
        shadow = true;
        alignment = Alignment.RIGHT;
        textColor = new Color(255, 255, 255, 255);
        bgColor = new Color(0, 0, 0, 100);
        for (Setting s : customSettings)
            s.reset();
        getConfig().save();
    }

    private static final ResourceLocation settingsIcon = new ResourceLocation("evergreenhud/textures/settings.png");
    private static final ResourceLocation deleteIcon = new ResourceLocation("evergreenhud/textures/delete.png");

    public void renderGuiOverlay(boolean selected) {
        Hitbox hitbox = getHitbox();
        GLRenderer.drawHollowRectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height, 1, (selected ? new Color(255, 255, 255, 175) : new Color(175, 175, 175, 100)));
        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.scale(0.02f, 0.02f, 1);
        mc.getTextureManager().bindTexture(settingsIcon);
        Gui.drawScaledCustomSizeModalRect((int)(hitbox.x / 0.02f), (int)((hitbox.y + hitbox.height - (384 * 0.02f)) / 0.02f), 0, 0, 384, 384, 384, 384, 384, 384);
        mc.getTextureManager().bindTexture(deleteIcon);
        Gui.drawScaledCustomSizeModalRect((int)((hitbox.x + hitbox.width - (384 * 0.02f)) / 0.02f), (int)((hitbox.y + hitbox.height - (384 * 0.02f)) / 0.02f), 0, 0, 384, 384, 384, 384, 384, 384);
        GlStateManager.popMatrix();
    }

    public void onMouseClicked(float mouseX, float mouseY) {
        Hitbox hitbox = getHitbox();
        if (mouseX >= hitbox.x && mouseX <= (hitbox.x) + (384 * 0.02f) && mouseY >= hitbox.y + (384 * 0.02f) && mouseY <= (hitbox.y) + (384 * 0.02f) + (384 * 0.02f)) {
            mc.displayGuiScreen(this.getElementConfigGui());
        }
        if (mouseX >= hitbox.x + hitbox.width - (384 * 0.02f) && mouseX <= (hitbox.x) + hitbox.width && mouseY >= hitbox.y + (384 * 0.02f) && mouseY <= (hitbox.y) + (384 * 0.02f) + (384 * 0.02f)) {
            this.setEnabled(false);
        }
    }

    public ElementConfig getConfig() {
        return config;
    }

    protected void addSettings(Setting... settings) {
        customSettings.addAll(Arrays.asList(settings));
    }

    public List<Setting> getCustomSettings() {
        return customSettings;
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

    public boolean showTitle() {
        return title;
    }

    public boolean showBrackets() {
        return brackets;
    }

    public boolean useChroma() {
        return chroma;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public Color getTextColor() {
        return textColor;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public Logger getLogger() {
        return logger;
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

    public void setTitle(boolean title) {
        this.title = title;
    }

    public void setBrackets(boolean brackets) {
        this.brackets = brackets;
    }

    public void setTextColor(Color color) {
        this.textColor = color;
    }

    public void setChroma(boolean chroma) {
        this.chroma = chroma;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

}
