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

package co.uk.isxander.evergreenhud.elements;

import co.uk.isxander.evergreenhud.event.Listenable;
import co.uk.isxander.evergreenhud.gui.screens.impl.GuiElementConfig;
import co.uk.isxander.evergreenhud.settings.impl.*;
import co.uk.isxander.evergreenhud.utils.Alignment;
import co.uk.isxander.xanderlib.utils.*;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import co.uk.isxander.evergreenhud.EvergreenHUD;
import co.uk.isxander.evergreenhud.gui.screens.impl.GuiMain;
import co.uk.isxander.evergreenhud.settings.Setting;
import net.apolloclient.utils.GLRenderer;
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

public abstract class Element extends Gui implements Listenable, Constants {

    @Override
    public boolean canReceiveEvents() {
        ElementManager manager = EvergreenHUD.getInstance().getElementManager();
        return manager.isEnabled();
    }

    /* Config */
    private Position pos;
    private boolean title;
    private boolean brackets;
    private boolean shadow;
    private boolean chroma;
    private boolean inverted;
    private float paddingWidth;
    private float paddingHeight;
    private Alignment alignment;
    private final List<Setting> customSettings;

    /* Color */
    private Color textColor;
    private Color bgColor;

    protected final Logger logger;
    private final ElementData meta;

    public Element() {
        this.customSettings = new ArrayList<>();
        resetSettings(false);
        this.meta = metadata();
        this.logger = LogManager.getLogger(getMetadata().getName());
        initialise();
    }

    public abstract void initialise();

    // performance: avoid repeat initialization
    protected abstract ElementData metadata();
    public final ElementData getMetadata() {
        return meta;
    }

    protected abstract String getValue();

    public abstract String getDisplayTitle();

    public ElementType getType() {
        return ElementType.getType(this);
    }

    public GuiElementConfig getElementConfigGui() {
        return new GuiElementConfig(this);
    }

    public String getDisplayString() {
        String builder = "";
        if (showBrackets())
            builder += "[";
        if (showTitle() && !isInverted() && useTitleSetting())
            builder += getDisplayTitle() + ": ";
        builder += getValue();
        if (showTitle() && isInverted() && useTitleSetting())
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
        HitBox2D hitbox = getHitbox(1, getPosition().getScale());
        float x = getPosition().getRawX(event.resolution);
        float y = getPosition().getRawY(event.resolution);
        GLRenderer.drawRectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height, getBgColor());
        GlStateManager.pushMatrix();
        GlStateManager.scale(getPosition().getScale(), getPosition().getScale(), 0);
        switch (getAlignment()) {
            case RIGHT:
                float posX = (x - mc.fontRendererObj.getStringWidth(getDisplayString())) / getPosition().getScale();
                float posY = y / getPosition().getScale();

                if (chroma)
                    GuiUtils.drawChromaString(mc.fontRendererObj, getDisplayString(), posX, posY, renderShadow(), false);
                else
                    mc.fontRendererObj.drawString(getDisplayString(), posX, posY, getTextColor().getRGB(), renderShadow());
                break;
            case CENTER:
                posX = x / getPosition().getScale();
                posY = y / getPosition().getScale();

                if (chroma)
                    GuiUtils.drawChromaString(mc.fontRendererObj, getDisplayString(), posX, posY, renderShadow(), true);
                else
                    drawCenteredString(mc.fontRendererObj, getDisplayString(), posX, posY, getTextColor().getRGB(), renderShadow());
                break;
            case LEFT:
                posX = x / getPosition().getScale();
                posY = y / getPosition().getScale();

                if (chroma)
                    GuiUtils.drawChromaString(mc.fontRendererObj, getDisplayString(), posX, posY, renderShadow(), false);
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

    public HitBox2D getHitbox(float posScale, float sizeScale) {
        HitBox2D hitbox = null;
        ScaledResolution res = new ScaledResolution(mc);
        float width = mc.fontRendererObj.getStringWidth(getDisplayString()) * sizeScale;
        float extraWidth = getPaddingWidth() * sizeScale;
        float height = mc.fontRendererObj.FONT_HEIGHT * sizeScale;
        float extraHeight = getPaddingHeight() * sizeScale;
        float x = getPosition().getRawX(res) / posScale;
        float y = getPosition().getRawY(res) / posScale;
        switch (getAlignment()) {
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

    public void resetSettings(boolean save) {
        pos = Position.getPositionWithRawPositioning(10, 10, 1, new ScaledResolution(mc));
        title = true;
        brackets = false;
        inverted = false;
        shadow = true;
        alignment = Alignment.LEFT;
        textColor = new Color(255, 255, 255, 255);
        bgColor = new Color(0, 0, 0, 100);
        paddingWidth = 4;
        paddingHeight = 4;
        for (Setting s : customSettings)
            s.reset();
        if (save)
            EvergreenHUD.getInstance().getElementManager().getElementConfig().save();
    }

    private static final ResourceLocation settingsIcon = new ResourceLocation("evergreenhud/textures/settings.png");
    private static final ResourceLocation deleteIcon = new ResourceLocation("evergreenhud/textures/delete.png");

    public void renderGuiOverlay(boolean selected) {
        HitBox2D hitbox = getHitbox(1, getPosition().getScale());
        // doesnt play well with background on so find a way to make it look accurate
        //GLRenderer.drawRectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height, new Color(255, 255, 255, 50));
        GLRenderer.drawHollowRectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height, 1, (selected ? new Color(255, 255, 255, 175) : new Color(175, 175, 175, 100)));
        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        double iconWidth = 384 * 0.02f * MathUtils.clamp(getPosition().getScale(), 0.75f, 1f);
        double iconHeight = 384 * 0.02f * MathUtils.clamp(getPosition().getScale(), 0.75f, 1f);
        mc.getTextureManager().bindTexture(settingsIcon);
        GLRenderer.drawModalRect(hitbox.x, hitbox.y + hitbox.height - iconHeight, 0, 0, 384, 384, iconWidth, iconHeight, 384, 384);
        mc.getTextureManager().bindTexture(deleteIcon);
        GLRenderer.drawModalRect(hitbox.x + hitbox.width - iconWidth, hitbox.y + hitbox.height - iconHeight, 0, 0, 384, 384, iconWidth, iconHeight, 384, 384);
        GlStateManager.popMatrix();
    }

    public void onMouseClicked(float mouseX, float mouseY) {
        HitBox2D hitbox = getHitbox(1, getPosition().getScale());
        double iconWidth = 384 * 0.02f * MathUtils.clamp01(getPosition().getScale());
        double iconHeight = 384 * 0.02f * MathUtils.clamp01(getPosition().getScale());
        if (mouseX >= hitbox.x && mouseX <= hitbox.x + iconWidth && mouseY >= hitbox.y + hitbox.height - iconHeight && mouseY <= hitbox.y + hitbox.height) {
            mc.displayGuiScreen(this.getElementConfigGui());
        }
        if (mouseX >= hitbox.x + hitbox.width - iconWidth && mouseX <= hitbox.x + hitbox.width && mouseY >= hitbox.y + hitbox.height - iconHeight && mouseY <= hitbox.y + hitbox.height) {
            EvergreenHUD.getInstance().getElementManager().removeElement(this);
            // Update the buttons so enabled is false
            if (mc.currentScreen instanceof GuiElementConfig) {
                GuiElementConfig gui = (GuiElementConfig) mc.currentScreen;
                if (gui.element.equals(this)) {
                    gui.addButtons();
                }
            }
        }
    }

    public BetterJsonObject generateJson() {
        BetterJsonObject settings = new BetterJsonObject();

        settings.addProperty("x", getPosition().getXScaled());
        settings.addProperty("y", getPosition().getYScaled());
        settings.addProperty("scale", getPosition().getScale());
        settings.addProperty("title", showTitle());
        settings.addProperty("brackets", showBrackets());
        settings.addProperty("inverted", isInverted());
        settings.addProperty("chroma", useChroma());
        settings.addProperty("shadow", renderShadow());
        settings.addProperty("align", getAlignment().ordinal());

        BetterJsonObject textCol = new BetterJsonObject();
        textCol.addProperty("r", getTextColor().getRed());
        textCol.addProperty("g", getTextColor().getGreen());
        textCol.addProperty("b", getTextColor().getBlue());
        settings.add("textColor", textCol);

        BetterJsonObject bgCol = new BetterJsonObject();
        bgCol.addProperty("r", getBgColor().getRed());
        bgCol.addProperty("g", getBgColor().getGreen());
        bgCol.addProperty("b", getBgColor().getBlue());
        bgCol.addProperty("a", getBgColor().getAlpha());
        bgCol.addProperty("padding_width", getPaddingWidth());
        bgCol.addProperty("padding_height", getPaddingHeight());
        settings.add("bgColor", bgCol);

        BetterJsonObject custom = new BetterJsonObject();
        for (Setting s : getCustomSettings()) {
            if (s instanceof BooleanSetting)
                custom.addProperty(s.getJsonKey(), ((BooleanSetting)s).get());
            else if (s instanceof IntegerSetting)
                custom.addProperty(s.getJsonKey(), ((IntegerSetting)s).get());
            else if (s instanceof DoubleSetting)
                custom.addProperty(s.getJsonKey(), ((DoubleSetting)s).get());
            else if (s instanceof ArraySetting)
                custom.addProperty(s.getJsonKey(), ((ArraySetting) s).getIndex());
            else if (s instanceof StringSetting)
                custom.addProperty(s.getJsonKey(), ((StringSetting)s).get());
            else if (s instanceof EnumSetting)
                custom.addProperty(s.getJsonKey(), ((EnumSetting<?>) s).getIndex());
        }
        settings.add("custom", custom);

        return settings;
    }

    public void loadJson(BetterJsonObject root) {
        getPosition().setScaledX(root.optFloat("x"));
        getPosition().setScaledY(root.optFloat("y"));
        getPosition().setScale(root.optFloat("scale", 1.0f));
        setTitle(root.optBoolean("title", true));
        setBrackets(root.optBoolean("brackets", false));
        setInverted(root.optBoolean("inverted", false));
        setChroma(root.optBoolean("chroma", false));
        setShadow(root.optBoolean("shadow", true));

        // Swapped Left and Right alignment
        // Code will be removed in v2.1
        if (root.has("alignment")) {
            EvergreenHUD.LOGGER.info("Converting Alignments");
            Alignment alignment = Alignment.values()[root.optInt("alignment", 0)];
            if (alignment == Alignment.LEFT) {
                alignment = Alignment.RIGHT;
            } else if (alignment == Alignment.RIGHT) {
                alignment = Alignment.LEFT;
            }
            setAlignment(alignment);
        } else {
            setAlignment(Alignment.values()[root.optInt("align", 0)]);
        }

        BetterJsonObject textColor = new BetterJsonObject(root.get("textColor").getAsJsonObject());
        setTextColor(new Color(textColor.optInt("r", 255), textColor.optInt("g", 255), textColor.optInt("b", 255)));

        BetterJsonObject bgColor = new BetterJsonObject(root.get("bgColor").getAsJsonObject());
        setBgColor(new Color(bgColor.optInt("r", 255), bgColor.optInt("g", 255), bgColor.optInt("b", 255), bgColor.optInt("a", 255)));
        setPaddingWidth(bgColor.optFloat("padding_width", 4));
        setPaddingHeight(bgColor.optFloat("padding_height", 4));

        BetterJsonObject custom = new BetterJsonObject(root.get("custom").getAsJsonObject());
        for (String key : custom.getAllKeys()) {
            for (Setting s : getCustomSettings()) {
                if (s.getJsonKey().equals(key)) {
                    if (s instanceof BooleanSetting)
                        ((BooleanSetting) s).set(custom.optBoolean(key));
                    else if (s instanceof IntegerSetting)
                        ((IntegerSetting) s).set(custom.optInt(key));
                    else if (s instanceof DoubleSetting)
                        ((DoubleSetting) s).set(custom.optDouble(key));
                    else if (s instanceof ArraySetting)
                        ((ArraySetting) s).set(custom.optInt(key));
                    else if (s instanceof StringSetting)
                        ((StringSetting) s).set(custom.optString(key));
                    else if (s instanceof EnumSetting)
                        ((EnumSetting<?>) s).set(custom.optInt(key));
                    break;
                }
            }
        }
        onSettingsLoad();
    }

    protected void onSettingsLoad() {

    }

    protected void addSettings(Setting... settings) {
        customSettings.addAll(Arrays.asList(settings));
    }

    public List<Setting> getCustomSettings() {
        return customSettings;
    }

    public void onAdded() {

    }
    public void onRemoved() {
        // You can't adjust a removed element
        if (mc.currentScreen instanceof GuiElementConfig) {
            GuiElementConfig configScreen = (GuiElementConfig) mc.currentScreen;
            if (configScreen.element.equals(this)) {
                mc.displayGuiScreen(new GuiMain());
            }
        }
    }

    public Position getPosition() {
        return pos;
    }

    public boolean useScaleSetting() {
        return true;
    }

    public boolean showTitle() {
        return title;
    }

    public boolean useTitleSetting() {
        return true;
    }

    public boolean showBrackets() {
        return brackets;
    }

    public boolean useBracketsSetting() {
        return true;
    }

    public boolean useChroma() {
        return chroma;
    }

    public boolean useChromaSetting() {
        return true;
    }

    public boolean isInverted() {
        return inverted;
    }

    public boolean useInvertedSetting() {
        return true;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public Color getTextColor() {
        return textColor;
    }

    public boolean useTextColorSetting() {
        return true;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public boolean useBgColorSetting() {
        return true;
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

    public boolean useAlignmentSetting() {
        return true;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public boolean renderShadow() {
        return shadow;
    }

    public boolean useShadowSetting() {
        return true;
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

    public float getPaddingWidth() {
        return paddingWidth;
    }

    public boolean usePaddingSetting() {
        return true;
    }

    public void setPaddingWidth(float paddingWidth) {
        this.paddingWidth = paddingWidth;
    }

    public float getPaddingHeight() {
        return paddingHeight;
    }

    public void setPaddingHeight(float paddingHeight) {
        this.paddingHeight = paddingHeight;
    }
}
