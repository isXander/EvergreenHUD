package co.uk.isxander.evergreenhud.elements.type;

import co.uk.isxander.evergreenhud.elements.RenderOrigin;
import co.uk.isxander.evergreenhud.settings.impl.*;
import co.uk.isxander.xanderlib.utils.GuiUtils;
import co.uk.isxander.xanderlib.utils.Resolution;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Renders a single line of text to the screen
 */
public abstract class SimpleTextElement extends TextElement {

    private BooleanSetting inverted;

    @Override
    public void render(float partialTicks, RenderOrigin origin) {
        super.render(partialTicks, origin);

        String displayString = getDisplayString();
        float scale = getPosition().getScale();
        boolean chroma = getChromaSetting().get();
        TextMode textMode = getTextModeSetting().get();
        int color = getTextColor().getRGB();

        float x = getPosition().getRawX(Resolution.get());
        float y = getPosition().getRawY(Resolution.get());
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 0);

        float posY = y / scale;
        switch (getAlignmentSetting().get()) {
            case RIGHT:
                float posX = (x - mc.fontRendererObj.getStringWidth(displayString)) / scale;

                GuiUtils.drawString(mc.fontRendererObj, displayString, posX, posY, textMode == TextMode.SHADOW, textMode == TextMode.BORDER, chroma, false, color);

                break;
            case CENTER:
                posX = x / scale;

                GuiUtils.drawString(mc.fontRendererObj, displayString, posX, posY, textMode == TextMode.SHADOW, textMode == TextMode.BORDER, chroma, true, color);

                break;
            case LEFT:
                posX = x / scale;

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
        boolean showTitle = !getTitleTextSetting().get().trim().isEmpty();
        StringBuilder builder = new StringBuilder();
        if (getBracketsSetting().get())
            builder.append("[");
        if (showTitle && !getInvertTitleSetting().get())
            builder.append(getTitleTextSetting().get()).append(": ");
        builder.append(getValue());
        if (showTitle && getInvertTitleSetting().get())
            builder.append(" ").append(getTitleTextSetting().get());
        if (getBracketsSetting().get())
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

        addSettings(inverted = new BooleanSetting("Invert Title", "Display", "If the title is rendered after the value.", false));
    }

    @Override
    public void loadJsonOld(BetterJsonObject root) {
        inverted.set(root.optBoolean("inverted", false));

        super.loadJsonOld(root);
    }

    public BooleanSetting getInvertTitleSetting() {
        return inverted;
    }

}
