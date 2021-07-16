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

package dev.isxander.evergreenhud.elements.type;

import dev.isxander.evergreenhud.settings.impl.*;
import dev.isxander.xanderlib.utils.GuiUtils;
import dev.isxander.xanderlib.utils.Resolution;
import dev.isxander.xanderlib.utils.json.BetterJsonObject;
import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Renders a single line of text to the screen
 */
public abstract class SimpleTextElement extends TextElement {

    @Getter private BooleanSetting invertedTitle;

    @Override
    public void render(float partialTicks, int origin) {
        super.render(partialTicks, origin);

        String displayString = getDisplayString();
        float scale = getPosition().getScale();
        boolean chroma = getChroma().get();
        TextMode textMode = getTextMode().get();
        int color = getTextColor().getRGB();

        float x = getPosition().getRawX(Resolution.get());
        float y = getPosition().getRawY(Resolution.get());
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 0);

        float posX = x / scale;
        float posY = y / scale;
        switch (getAlignment().get()) {
            case RIGHT:
                posX = (x - mc.fontRendererObj.getStringWidth(displayString)) / scale;
                GuiUtils.drawString(mc.fontRendererObj, displayString, posX, posY, textMode == TextMode.SHADOW, textMode == TextMode.BORDER, chroma, false, color);

                break;
            case CENTER:
                GuiUtils.drawString(mc.fontRendererObj, displayString, posX, posY, textMode == TextMode.SHADOW, textMode == TextMode.BORDER, chroma, true, color);

                break;
            case LEFT:
                GuiUtils.drawString(mc.fontRendererObj, displayString, posX, posY, textMode == TextMode.SHADOW, textMode == TextMode.BORDER, chroma, false, color);

                break;
        }
        GlStateManager.popMatrix();
    }

    /**
     * Combines the value and display title together
     *
     * @return the text that will be rendered
     */
    public String getDisplayString() {
        boolean showTitle = !getTitleText().get().trim().isEmpty();
        StringBuilder builder = new StringBuilder();
        if (getBrackets().get())
            builder.append("[");
        if (showTitle && !getInvertedTitle().get())
            builder.append(getTitleText().get()).append(": ");
        builder.append(getValue());
        if (showTitle && getInvertedTitle().get())
            builder.append(" ").append(getTitleText().get());
        if (getBrackets().get())
            builder.append("]");
        return builder.toString();
    }

    /**
     * @return the text to display on the HUD element
     */
    protected abstract String getValue();

    @Override
    protected float getHitBoxWidth() {
        return Math.max(mc.fontRendererObj.getStringWidth(getDisplayString()), 10);
    }

    @Override
    protected float getHitBoxHeight() {
        return mc.fontRendererObj.FONT_HEIGHT;
    }

    @Override
    protected void registerDefaultSettings() {
        super.registerDefaultSettings();

        addSettings(invertedTitle = new BooleanSetting("Invert Title", "Display", "If the title is rendered after the value.", false));
    }

    @Override
    public void loadJsonOld(BetterJsonObject root) {
        invertedTitle.set(root.optBoolean("inverted", false));

        super.loadJsonOld(root);
    }

}
