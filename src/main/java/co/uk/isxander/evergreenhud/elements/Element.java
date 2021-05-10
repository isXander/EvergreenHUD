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

    private final List<Setting> customSettings;

    /* Config */
    private Position pos;
    private BooleanSetting title;
    private BooleanSetting brackets;
    private BooleanSetting shadow;
    private BooleanSetting chroma;
    private BooleanSetting inverted;
    private FloatSetting paddingWidth;
    private FloatSetting paddingHeight;
    private EnumSetting<Alignment> alignment;

    /* Color */
    private IntegerSetting textR;
    private IntegerSetting textG;
    private IntegerSetting textB;

    private IntegerSetting backR;
    private IntegerSetting backG;
    private IntegerSetting backB;
    private IntegerSetting backA;

    protected final Logger logger;
    private final ElementData meta;

    public Element() {
        this.customSettings = new ArrayList<>();
        registerDefaultSettings();
        resetSettings(false);
        this.meta = metadata();
        this.logger = LogManager.getLogger(getMetadata().getName());
        initialise();
    }

    private void registerDefaultSettings() {
        addSettings(new IntegerSetting("Scale", "How big the element is displayed.", 100, 50, 200, "%", false) {
            @Override
            public int get() {
                return (int) (getPosition().getScale() * 100f);
            }

            @Override
            public void set(int newVal) {
                getPosition().setScale(newVal / 100f);
            }

            @Override
            public boolean shouldAddToConfig() {
                return false;
            }
        });

        addSettings(brackets = new BooleanSetting("Brackets", "If there are square brackets before and after the text.", false) {
            @Override
            public boolean isDisabled() {
                return !useBracketsSetting();
            }
        });
        addSettings(title = new BooleanSetting("Title", "If the text has the element name in it.", true) {
            @Override
            public boolean isDisabled() {
                return !useTitleSetting();
            }
        });
        addSettings(inverted = new BooleanSetting("Invert Title", "If the title is rendered after the value.", false) {
            @Override
            public boolean isDisabled() {
                return !useInvertedSetting();
            }
        });

        addSettings(textR = new IntegerSetting("Text Red", "How much red is in the color of the text.", 255, 0, 255, "") {
            @Override
            public boolean isDisabled() {
                return !useTextColorSetting();
            }
        });
        addSettings(textG = new IntegerSetting("Text Green", "How much green is in the color of the text.", 255, 0, 255, "") {
            @Override
            public boolean isDisabled() {
                return !useTextColorSetting();
            }
        });
        addSettings(textB = new IntegerSetting("Text Blue", "How much blue is in the color of the text.", 255, 0, 255, "") {
            @Override
            public boolean isDisabled() {
                return !useTextColorSetting();
            }
        });
        addSettings(chroma = new BooleanSetting("Chroma Text", "If the color of the text is a multicolored mess.", false) {
            @Override
            public boolean isDisabled() {
                return !useChromaSetting();
            }
        });

        addSettings(backR = new IntegerSetting("Background Red", "How much red is in the color of the background.", 0, 0, 255, "") {
            @Override
            public boolean isDisabled() {
                return !useBgColorSetting();
            }
        });
        addSettings(backG = new IntegerSetting("Background Green", "How much green is in the color of the background.", 0, 0, 255, "") {
            @Override
            public boolean isDisabled() {
                return !useBgColorSetting();
            }
        });
        addSettings(backB = new IntegerSetting("Background Blue", "How much blue is in the color of the background.", 0, 0, 255, "") {
            @Override
            public boolean isDisabled() {
                return !useBgColorSetting();
            }
        });
        addSettings(backA = new IntegerSetting("Background Alpha", "How much alpha is in the color of the background.", 100, 0, 255, "") {
            @Override
            public boolean isDisabled() {
                return !useBgColorSetting();
            }
        });

        addSettings(shadow = new BooleanSetting("Shadow", "If the text has a shadow.", true) {
            @Override
            public boolean isDisabled() {
                return !useShadowSetting();
            }
        });
        addSettings(alignment = new EnumSetting<Alignment>("Alignment", "When the text grows or shrinks in size, which way the element will move.", Alignment.LEFT) {
            @Override
            public boolean isDisabled() {
                return !useAlignmentSetting();
            }
        });

        addSettings(paddingWidth = new FloatSetting("Padding Width", "How much extra width the background box will have.", 4f, 0f, 12f, "") {
            @Override
            public boolean isDisabled() {
                return !usePaddingSetting();
            }
        });
        addSettings(paddingHeight = new FloatSetting("Padding Height", "How much extra height the background box will have.", 4f, 0f, 12f, "") {
            @Override
            public boolean isDisabled() {
                return !usePaddingSetting();
            }
        });
    }

    public abstract void initialise();

    // performance: avoid repeat initialization
    protected abstract ElementData metadata();
    public final ElementData getMetadata() {
        return meta;
    }

    /**
     * @return the text to display on the HUD element
     */
    protected abstract String getValue();

    /**
     * @return the prefix/suffix of the value
     */
    public abstract String getDisplayTitle();

    public final String getType() {
        return EvergreenHUD.getInstance().getElementManager().getElementIdentifier(this);
    }

    public GuiElementConfig getElementConfigGui() {
        return new GuiElementConfig(this);
    }

    /**
     * Combines the value and display title together
     *
     * @return the text that will be rendered
     */
    public String getDisplayString() {
        String builder = "";
        if (showBrackets().get())
            builder += "[";
        if (showTitle().get() && !isInverted().get() && useTitleSetting())
            builder += getDisplayTitle() + ": ";
        builder += getValue();
        if (showTitle().get() && isInverted().get() && useTitleSetting())
            builder += " " + getDisplayTitle();
        if (showBrackets().get())
            builder += "]";
        return builder;
    }

    /**
     * Renders to the screen.
     *
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
        switch (getAlignment().get()) {
            case RIGHT:
                float posX = (x - mc.fontRendererObj.getStringWidth(getDisplayString())) / getPosition().getScale();
                float posY = y / getPosition().getScale();

                if (useChroma().get())
                    GuiUtils.drawChromaString(mc.fontRendererObj, getDisplayString(), posX, posY, renderShadow().get(), false);
                else
                    mc.fontRendererObj.drawString(getDisplayString(), posX, posY, getTextColor().getRGB(), renderShadow().get());
                break;
            case CENTER:
                posX = x / getPosition().getScale();
                posY = y / getPosition().getScale();

                if (useChroma().get())
                    GuiUtils.drawChromaString(mc.fontRendererObj, getDisplayString(), posX, posY, renderShadow().get(), true);
                else
                    GuiUtils.drawCenteredString(mc.fontRendererObj, getDisplayString(), posX, posY, getTextColor().getRGB(), renderShadow().get());
                break;
            case LEFT:
                posX = x / getPosition().getScale();
                posY = y / getPosition().getScale();

                if (useChroma().get())
                    GuiUtils.drawChromaString(mc.fontRendererObj, getDisplayString(), posX, posY, renderShadow().get(), false);
                else
                    mc.fontRendererObj.drawString(getDisplayString(), posX, posY, getTextColor().getRGB(), renderShadow().get());
                break;
        }
        GlStateManager.popMatrix();
        mc.mcProfiler.endSection();
    }

    /**
     * Gets the box around the text
     *
     * @param posScale the gl scale
     * @param sizeScale the modified scale
     * @return hitbox for rendering & gui
     */
    public HitBox2D getHitbox(float posScale, float sizeScale) {
        HitBox2D hitbox = null;
        ScaledResolution res = new ScaledResolution(mc);
        float width = Math.max(mc.fontRendererObj.getStringWidth(getDisplayString()), 10) * sizeScale;
        float extraWidth = getPaddingWidth().get() * sizeScale;
        float height = mc.fontRendererObj.FONT_HEIGHT * sizeScale;
        float extraHeight = getPaddingHeight().get() * sizeScale;
        float x = getPosition().getRawX(res) / posScale;
        float y = getPosition().getRawY(res) / posScale;
        switch (getAlignment().get()) {
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

    /**
     * Reset all settings to their default value
     *
     * @param save whether or not to save the new config
     */
    public void resetSettings(boolean save) {
        pos = Position.getPositionWithRawPositioning(10, 10, 1, new ScaledResolution(mc));

        for (Setting s : customSettings)
            s.reset();
        if (save)
            EvergreenHUD.getInstance().getElementManager().getElementConfig().save();
    }

    private static final ResourceLocation settingsIcon = new ResourceLocation("evergreenhud/textures/settings.png");
    private static final ResourceLocation deleteIcon = new ResourceLocation("evergreenhud/textures/delete.png");

    public void renderGuiOverlay(boolean selected) {
        HitBox2D hitbox = getHitbox(1, getPosition().getScale());
        // doesnt play well with background on so find a way to make it look good
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

        BetterJsonObject custom = new BetterJsonObject();
        for (Setting s : getCustomSettings()) {
            if (!s.shouldAddToConfig()) continue;

            if (s instanceof BooleanSetting)
                custom.addProperty(s.getJsonKey(), ((BooleanSetting)s).get());
            else if (s instanceof IntegerSetting)
                custom.addProperty(s.getJsonKey(), ((IntegerSetting)s).get());
            else if (s instanceof FloatSetting)
                custom.addProperty(s.getJsonKey(), ((FloatSetting)s).get());
            else if (s instanceof ArraySetting)
                custom.addProperty(s.getJsonKey(), ((ArraySetting) s).getIndex());
            else if (s instanceof StringSetting)
                custom.addProperty(s.getJsonKey(), ((StringSetting)s).get());
            else if (s instanceof EnumSetting)
                custom.addProperty(s.getJsonKey(), ((EnumSetting<?>) s).getIndex());
        }
        settings.add("dynamic", custom);

        return settings;
    }

    public void loadJson(BetterJsonObject root) {
        getPosition().setScaledX(root.optFloat("x"));
        getPosition().setScaledY(root.optFloat("y"));
        getPosition().setScale(root.optFloat("scale", 1.0f));

        BetterJsonObject custom = new BetterJsonObject(root.get("dynamic").getAsJsonObject());
        for (String key : custom.getAllKeys()) {
            for (Setting s : getCustomSettings()) {
                if (s.getJsonKey().equals(key)) {
                    if (s instanceof BooleanSetting)
                        ((BooleanSetting) s).set(custom.optBoolean(key));
                    else if (s instanceof IntegerSetting)
                        ((IntegerSetting) s).set(custom.optInt(key));
                    else if (s instanceof FloatSetting)
                        ((FloatSetting) s).set(custom.optFloat(key));
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

    public BooleanSetting showTitle() {
        return title;
    }

    public boolean useTitleSetting() {
        return true;
    }

    public BooleanSetting showBrackets() {
        return brackets;
    }

    public boolean useBracketsSetting() {
        return true;
    }

    public BooleanSetting useChroma() {
        return chroma;
    }

    public boolean useChromaSetting() {
        return true;
    }

    public BooleanSetting isInverted() {
        return inverted;
    }

    public boolean useInvertedSetting() {
        return true;
    }

    public Color getTextColor() {
       return new Color(textR.get(), textG.get(), textB.get(), 255);
    }

    public void setTextColor(int r, int g, int b) {
        textR.set(r);
        textG.set(g);
        textB.set(b);
    }

    public boolean useTextColorSetting() {
        return true;
    }

    public Color getBgColor() {
        return new Color(backR.get(), backG.get(), backB.get(), backA.get());
    }

    public void setBgColor(int r, int g, int b, int a) {
        backR.set(r);
        backG.set(g);
        backB.set(b);
        backA.set(a);
    }

    public boolean useBgColorSetting() {
        return true;
    }

    public EnumSetting<Alignment> getAlignment() {
        return alignment;
    }

    public boolean useAlignmentSetting() {
        return true;
    }

    public BooleanSetting renderShadow() {
        return shadow;
    }

    public boolean useShadowSetting() {
        return true;
    }

    public FloatSetting getPaddingWidth() {
        return paddingWidth;
    }

    public boolean usePaddingSetting() {
        return true;
    }

    public FloatSetting getPaddingHeight() {
        return paddingHeight;
    }

    public void setPosition(Position newPos) {
        this.pos = newPos;
    }

}
