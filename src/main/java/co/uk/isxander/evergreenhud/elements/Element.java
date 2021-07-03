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

package co.uk.isxander.evergreenhud.elements;

import co.uk.isxander.evergreenhud.gui.screens.GuiScreenElements;
import co.uk.isxander.evergreenhud.utils.SnapPoint;
import co.uk.isxander.evergreenhud.event.Listenable;
import co.uk.isxander.evergreenhud.gui.screens.impl.GuiElementConfig;
import co.uk.isxander.evergreenhud.settings.impl.*;
import co.uk.isxander.xanderlib.utils.*;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import co.uk.isxander.evergreenhud.EvergreenHUD;
import co.uk.isxander.evergreenhud.gui.screens.impl.GuiMain;
import co.uk.isxander.evergreenhud.settings.Setting;
import lombok.Getter;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Element extends Gui implements Listenable, Constants {

    @Getter
    private static final ElementUtilitySharer utilitySharer = new ElementUtilitySharer();

    @Override
    public boolean canReceiveEvents() {
        ElementManager manager = EvergreenHUD.getInstance().getElementManager();
        return manager.isEnabled() || mc.currentScreen instanceof GuiScreenElements;
    }

    @Getter private final List<Setting> customSettings;
    @Getter private final List<SnapPoint> snapPoints;

    /* Config */
    @Getter private Position position;
    @Getter private BooleanSetting showInChat;
    @Getter private BooleanSetting showUnderGui;
    @Getter private BooleanSetting showInDebug;

    protected final Logger logger;
    private final ElementData meta;

    public Element() {
        this.customSettings = new ArrayList<>();
        registerDefaultSettings();
        resetSettings(false);
        this.snapPoints = new ArrayList<>();
        registerSnapPoints();
        this.meta = metadata();
        this.logger = LogManager.getLogger(getMetadata().getName());
        initialise();
    }

    protected void registerSnapPoints() {
        snapPoints.add(new SnapPoint(this, 0, 0));
        snapPoints.add(new SnapPoint(this, 0, 1));
        snapPoints.add(new SnapPoint(this, 1, 0));
        snapPoints.add(new SnapPoint(this, 1, 1));
        snapPoints.add(new SnapPoint(this, 0, 0.5f));
        snapPoints.add(new SnapPoint(this, 1, 0.5f));
        snapPoints.add(new SnapPoint(this, 0.5f, 0));
        snapPoints.add(new SnapPoint(this, 0.5f, 1));
    }

    protected void registerDefaultSettings() {
        addSettings(new IntegerSetting("Scale", "Display", "How big the element is displayed.", 100, 50, 200, "%", false) {
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
        addSettings(showInChat = new BooleanSetting("Show In Chat", "Visibility", "Whether or not element should be displayed in the chat menu. (Takes priority over show under gui)", false));
        addSettings(showInDebug = new BooleanSetting("Show In F3", "Visibility", "Whether or not element should be displayed when the debug menu is open.", false));
        addSettings(showUnderGui = new BooleanSetting("Show Under GUIs", "Visibility", "Whether or not element should be displayed when you have a gui open.", true));
    }

    public void initialise() {

    }

    // performance: avoid repeat initialization
    protected abstract ElementData metadata();
    public final ElementData getMetadata() {
        return meta;
    }

    public final String getType() {
        return EvergreenHUD.getInstance().getElementManager().getElementIdentifier(this);
    }

    public GuiElementConfig getElementConfigGui(GuiScreen parent) {
        return new GuiElementConfig(this, parent);
    }

    /**
     * Renders to the screen.
     */
    public abstract void render(float partialTicks, int origin);

    /**
     * Gets the box around the text
     *
     * @param gl the gl scale
     * @param sizeScale the modified scale
     * @return hitbox for rendering & gui
     */
    public abstract HitBox2D calculateHitBox(float gl, float sizeScale);

    protected float getHitBoxWidth() {
        return 10f;
    }

    protected float getHitBoxHeight() {
        return 10f;
    }

    /**
     * Reset all settings to their default value
     *
     * @param save whether or not to save the new config
     */
    public void resetSettings(boolean save) {
        position = Position.getPositionWithRawPositioning(10, 10, 1, Resolution.get());

        for (Setting s : customSettings)
            s.reset();
        if (save)
            EvergreenHUD.getInstance().getElementManager().getElementConfig().save();
    }

    private static final ResourceLocation settingsIcon = new ResourceLocation("evergreenhud", "textures/settings.png");
    private static final ResourceLocation deleteIcon = new ResourceLocation("evergreenhud","textures/delete.png");

    public void renderGuiOverlay(boolean selected) {
        HitBox2D hitbox = calculateHitBox(1, getPosition().getScale());
        // doesnt play well with background on so find a way to make it look good
        //GLRenderer.drawRectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height, new Color(255, 255, 255, 50));
        GLRenderer.drawHollowRectangle(hitbox.x - 1, hitbox.y - 1, hitbox.width + 2, hitbox.height + 2, 1, (selected ? new Color(255, 255, 255, 175) : new Color(175, 175, 175, 100)));
        GlStateManager.pushMatrix();

        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        double iconWidth = 384 * 0.02f * MathUtils.clamp(getPosition().getScale(), 0.75f, 1f);
        double iconHeight = 384 * 0.02f * MathUtils.clamp(getPosition().getScale(), 0.75f, 1f);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR, 1, 0);
        GlStateManager.enableAlpha();

        mc.getTextureManager().bindTexture(settingsIcon);
        GLRenderer.drawModalRect(hitbox.x, hitbox.y + hitbox.height - iconHeight, 0, 0, 384, 384, iconWidth, iconHeight, 384, 384);

        mc.getTextureManager().bindTexture(deleteIcon);
        GLRenderer.drawModalRect(hitbox.x + hitbox.width - iconWidth, hitbox.y + hitbox.height - iconHeight, 0, 0, 384, 384, iconWidth, iconHeight, 384, 384);

        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();
    }

    public void onMouseClicked(float mouseX, float mouseY) {
        HitBox2D hitbox = calculateHitBox(1, getPosition().getScale());
        double iconWidth = 384 * 0.02f * MathUtils.clamp01(getPosition().getScale());
        double iconHeight = 384 * 0.02f * MathUtils.clamp01(getPosition().getScale());
        if (mouseX >= hitbox.x && mouseX <= hitbox.x + iconWidth && mouseY >= hitbox.y + hitbox.height - iconHeight && mouseY <= hitbox.y + hitbox.height) {
            mc.displayGuiScreen(this.getElementConfigGui(new GuiMain(null)));
        }
        if (mouseX >= hitbox.x + hitbox.width - iconWidth && mouseX <= hitbox.x + hitbox.width && mouseY >= hitbox.y + hitbox.height - iconHeight && mouseY <= hitbox.y + hitbox.height) {
            EvergreenHUD.getInstance().getElementManager().removeElement(this);
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

            if (s instanceof BooleanSetting) {
                BooleanSetting setting = (BooleanSetting) s;
                // no need to save the default value
                if (setting.getDefault() == setting.get()) continue;

                custom.addProperty(setting.getJsonKey(), setting.get());
            } else if (s instanceof IntegerSetting) {
                IntegerSetting setting = (IntegerSetting) s;
                // no need to save the default value
                if (setting.getDefault() == setting.get()) continue;

                custom.addProperty(setting.getJsonKey(), setting.get());
            } else if (s instanceof FloatSetting) {
                FloatSetting setting = (FloatSetting) s;
                // no need to save the default value
                if (setting.getDefault() == setting.get()) continue;

                custom.addProperty(setting.getJsonKey(), setting.get());
            } else if (s instanceof ArraySetting) {
                ArraySetting setting = (ArraySetting) s;
                // no need to save the default value
                if (setting.getIndex() == setting.getDefault()) continue;

                custom.addProperty(setting.getJsonKey(), setting.getIndex());
            } else if (s instanceof StringSetting) {
                StringSetting setting = (StringSetting) s;
                // no need to save the default value
                if (setting.get().equals(setting.getDefault())) continue;

                custom.addProperty(setting.getJsonKey(), setting.get());
            } else if (s instanceof EnumSetting) {
                EnumSetting<?> setting = (EnumSetting<?>) s;
                // no need to save the default value
                if (setting.getIndex() == setting.getDefaultIndex()) continue;

                custom.addProperty(setting.getJsonKey(), setting.getIndex());
            }
        }
        settings.add("dynamic", custom);

        return settings;
    }

    public void loadJson(BetterJsonObject root) {
        getPosition().setScaledX(root.optFloat("x", 0.5f));
        getPosition().setScaledY(root.optFloat("y", 0.5f));
        getPosition().setScale(root.optFloat("scale", 1.0f));

        BetterJsonObject custom = new BetterJsonObject(root.get("dynamic").getAsJsonObject());
        for (String key : custom.getAllKeys()) {
            for (Setting s : getCustomSettings()) {
                if (s.getJsonKey().equals(key)) {
                    if (s instanceof BooleanSetting) {
                        BooleanSetting setting = (BooleanSetting) s;
                        setting.set(custom.optBoolean(key, setting.getDefault()));
                    } else if (s instanceof IntegerSetting) {
                        IntegerSetting setting = (IntegerSetting) s;
                        setting.set((int) MathUtils.clamp(custom.optInt(key, setting.getDefault()), setting.getMin(), setting.getMax()));
                    } else if (s instanceof FloatSetting) {
                        FloatSetting setting = (FloatSetting) s;
                        setting.set(MathUtils.clamp(custom.optFloat(key, setting.getDefault()), setting.getMin(), setting.getMax()));
                    } else if (s instanceof ArraySetting) {
                        ArraySetting setting = (ArraySetting) s;
                        setting.set((int) MathUtils.clamp(custom.optInt(key, setting.getDefault()), 0, setting.options().size() - 1));
                    } else if (s instanceof StringSetting) {
                        StringSetting setting = (StringSetting) s;
                        setting.set(custom.optString(key, setting.getDefault()));
                    } else if (s instanceof EnumSetting) {
                        EnumSetting<?> setting = (EnumSetting<?>) s;
                        setting.set(custom.optInt(key, setting.getDefaultIndex()));
                    }
                    break;
                }
            }
        }
        onSettingsLoad();
    }

    public void loadJsonOld(BetterJsonObject root) {
        if (root.has("custom")) {
            BetterJsonObject custom = root.getObj("custom");
            root.getData().remove("custom");
            root.add("dynamic", custom);
        }

        loadJson(root);
    }

    protected void onSettingsLoad() {

    }

    protected void addSettings(Setting... settings) {
        customSettings.addAll(Arrays.asList(settings));
    }

    public void onAdded() {

    }
    public void onRemoved() {
        // You can't adjust a removed element
        if (mc.currentScreen instanceof GuiElementConfig) {
            GuiElementConfig configScreen = (GuiElementConfig) mc.currentScreen;
            if (configScreen.element.equals(this)) {
                mc.displayGuiScreen(new GuiMain(null));
            }
        }

        // clear the utilities
        getUtilitySharer().unregisterAllForObject(this);
    }

    public void drawTexturedModalRectF(float x, float y, int textureX, int textureY, float width, float height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, this.zLevel).tex(textureX * f, (textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, this.zLevel).tex((textureX + width) * f, (textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y, this.zLevel).tex((textureX + width) * f, textureY * f1).endVertex();
        worldrenderer.pos(x + 0, y + 0, this.zLevel).tex(textureX * f, textureY * f1).endVertex();
        tessellator.draw();
    }
}
