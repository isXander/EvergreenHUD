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

package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.elements.RenderOrigin;
import co.uk.isxander.evergreenhud.elements.type.BackgroundElement;
import co.uk.isxander.evergreenhud.elements.type.TextElement;
import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.evergreenhud.settings.impl.EnumSetting;
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.evergreenhud.settings.impl.StringSetting;
import co.uk.isxander.xanderlib.utils.GuiUtils;
import co.uk.isxander.xanderlib.utils.Resolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ElementPotionHUD extends BackgroundElement {

    public static final int ICON_SIZE = 18;

    public IntegerSetting titleTextR;
    public IntegerSetting titleTextG;
    public IntegerSetting titleTextB;
    public EnumSetting<TextElement.TextMode> titleTextMode;
    public BooleanSetting titleTextChroma;
    public BooleanSetting titleTextBold;
    public BooleanSetting titleTextUnderline;
    public BooleanSetting titleTextItalic;
    public BooleanSetting showTitle;
    public BooleanSetting showAmplifier;
    public EnumSetting<AmplifierMode> amplifierText;
    public BooleanSetting showAmplifierLevelOne;

    public IntegerSetting timeTextR;
    public IntegerSetting timeTextG;
    public IntegerSetting timeTextB;
    public EnumSetting<TextElement.TextMode> timeTextMode;
    public BooleanSetting timeTextChroma;
    public BooleanSetting timeTextUnderline;
    public BooleanSetting timeTextItalic;
    public BooleanSetting showTime;

    public BooleanSetting showSpeed;
    public BooleanSetting showSlowness;
    public BooleanSetting showHaste;
    public BooleanSetting showMiningFatigue;
    public BooleanSetting showStrength;
    public BooleanSetting showJumpBoost;
    public BooleanSetting showNausea;
    public BooleanSetting showRegeneration;
    public BooleanSetting showResistance;
    public BooleanSetting showFireResistance;
    public BooleanSetting showWaterBreathing;
    public BooleanSetting showInvisibility;
    public BooleanSetting showBlindness;
    public BooleanSetting showNightVision;
    public BooleanSetting showHunger;
    public BooleanSetting showWeakness;
    public BooleanSetting showPoison;
    public BooleanSetting showWither;
    public BooleanSetting showHealthBoost;
    public BooleanSetting showAbsorption;
    public BooleanSetting showSaturation;

    public BooleanSetting showPermanent;
    public StringSetting permanentText;

    public BooleanSetting showIcon;
    public EnumSetting<VerticalAlignmentMode> verticalAlign;
    public EnumSetting<SortingMode> sortingMode;
    public IntegerSetting verticalSpacing;


    private float width = 10f;
    private float height = 10f;

    @Override
    public void initialise() {
        addSettings(titleTextR            = new IntegerSetting("Title Red",        "Title", "The red channel of the title text.",   255, 0, 255, ""));
        addSettings(titleTextG            = new IntegerSetting("Title Green",      "Title", "The green channel of the title text.", 255, 0, 255, ""));
        addSettings(titleTextB            = new IntegerSetting("Title Blue",       "Title", "The blue channel of the title text.",  50,  0, 255, ""));
        addSettings(titleTextMode         = new EnumSetting<>( "Title Mode",       "Title", "How should the text be rendered.", TextElement.TextMode.SHADOW));
        addSettings(titleTextChroma       = new BooleanSetting("Title Chroma",     "Title", "If the color of the title is a multicolored mess.",        false));
        addSettings(titleTextBold         = new BooleanSetting("Title Bold",       "Title", "If the title is bold.",                                    true));
        addSettings(titleTextUnderline    = new BooleanSetting("Title Underlined", "Title", "If the title is underlined.",                              false));
        addSettings(showTitle             = new BooleanSetting("Show Title",       "Title", "Show the title.",                                          true));
        addSettings(titleTextItalic       = new BooleanSetting("Title Italic",     "Title", "If the title is italic.",                                  false));
        addSettings(showAmplifier         = new BooleanSetting("Show Amplifier",   "Title", "Show the potion amplifier.",                               true));
        addSettings(showAmplifierLevelOne = new BooleanSetting("Show LVL 1",       "Title", "Whether or not the element displays amplifier level one.", false));
        addSettings(amplifierText         = new EnumSetting<>( "Amplifier Text",   "Title", "How the amplifier should be displayed.", AmplifierMode.ARABIC));

        addSettings(timeTextR             = new IntegerSetting("Time Red",               "Time", "The red channel of the time text.",   255, 0, 255, ""));
        addSettings(timeTextG             = new IntegerSetting("Time Green",             "Time", "The green channel of the time text.", 255, 0, 255, ""));
        addSettings(timeTextB             = new IntegerSetting("Time Blue",              "Time", "The blue channel of the time text.",  50,  0, 255, ""));
        addSettings(timeTextMode          = new EnumSetting<>( "Time Mode",              "Time", "How should the text be rendered.", TextElement.TextMode.SHADOW));
        addSettings(timeTextChroma        = new BooleanSetting("Time Chroma",            "Time", "If the color of the time is a multicolored mess.", false));
        addSettings(timeTextUnderline     = new BooleanSetting("Time Underlined",        "Time", "If the time is underlined.",                       false));
        addSettings(timeTextItalic        = new BooleanSetting("Time Italic",            "Time", "If the time is italic.",                           false));
        addSettings(showTime              = new BooleanSetting("Show Time",              "Time", "Show the time.",                                   true));
        addSettings(showPermanent         = new BooleanSetting("Show Permanent Effects", "Time", "Show permanent effects on the HUD.",               true));
        addSettings(permanentText         = new StringSetting( "Permanent Text",         "Time", "What text is displayed in place of the time if the potion is permanent.", "**:**"));

        addSettings(showSpeed             = new BooleanSetting("Speed",          "Whitelist", "Display the speed effect on the HUD.",           true));
        addSettings(showSlowness          = new BooleanSetting("Slowness",       "Whitelist", "Display the slowness effect on the HUD.",        true));
        addSettings(showHaste             = new BooleanSetting("Haste",          "Whitelist", "Display the haste effect on the HUD.",           true));
        addSettings(showMiningFatigue     = new BooleanSetting("Mining Fatigue", "Whitelist", "Display the mining fatigue effect on the HUD.",  true));
        addSettings(showStrength          = new BooleanSetting("Strength",       "Whitelist", "Display the strength effect on the HUD.",        true));
        addSettings(showJumpBoost         = new BooleanSetting("Jump Boost",     "Whitelist", "Display the jump boost effect on the HUD.",      true));
        addSettings(showNausea            = new BooleanSetting("Nausea",         "Whitelist", "Display the nausea effect on the HUD.",          true));
        addSettings(showRegeneration      = new BooleanSetting("Regeneration",   "Whitelist", "Display the regeneration effect on the HUD.",    true));
        addSettings(showResistance        = new BooleanSetting("Resistance",     "Whitelist", "Display the resistance effect on the HUD.",      true));
        addSettings(showFireResistance    = new BooleanSetting("Fire Res",       "Whitelist", "Display the fire resistance effect on the HUD.", true));
        addSettings(showWaterBreathing    = new BooleanSetting("Water Breathe",  "Whitelist", "Display the water breathing effect on the HUD.", true));
        addSettings(showInvisibility      = new BooleanSetting("Invisibility",   "Whitelist", "Display the invisibility effect on the HUD.",    true));
        addSettings(showBlindness         = new BooleanSetting("Blindness",      "Whitelist", "Display the blindness effect on the HUD.",       true));
        addSettings(showNightVision       = new BooleanSetting("Night Vision",   "Whitelist", "Display the night vision effect on the HUD.",    true));
        addSettings(showHunger            = new BooleanSetting("Hunger",         "Whitelist", "Display the hunger effect on the HUD.",          true));
        addSettings(showWeakness          = new BooleanSetting("Weakness",       "Whitelist", "Display the weakness effect on the HUD.",        true));
        addSettings(showPoison            = new BooleanSetting("Poison",         "Whitelist", "Display the poison effect on the HUD.",          true));
        addSettings(showWither            = new BooleanSetting("Wither",         "Whitelist", "Display the wither effect on the HUD.",          true));
        addSettings(showHealthBoost       = new BooleanSetting("Health Boost",   "Whitelist", "Display the health boost effect on the HUD.",    true));
        addSettings(showAbsorption        = new BooleanSetting("Absorption",     "Whitelist", "Display the absorption effect on the HUD.",      true));
        addSettings(showSaturation        = new BooleanSetting("Saturation",     "Whitelist", "Display the saturation effect on the HUD.",      true));

        addSettings(showIcon              = new BooleanSetting("Show Icon",        "Other", "Show the status effect icon.", true));
        addSettings(verticalAlign         = new EnumSetting<>( "Vertical Align",   "Other", "How the effects should be aligned vertically.",   VerticalAlignmentMode.DOWN));
        addSettings(sortingMode           = new EnumSetting<>( "Sort",             "Other", "In what way the effects shall be sorted.",        SortingMode.DURATION));
        addSettings(verticalSpacing       = new IntegerSetting("Vertical Spacing", "Other", "How far apart each potion will be.", 2, 1, 9, " px"));
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("PotionHUD", "Displays all current potion effects.", "Advanced");
    }

    @Override
    public void render(float partialTicks, int origin) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.disableLighting();

        float x = getPosition().getRawX(Resolution.get());
        float y = getPosition().getRawY(Resolution.get());

        List<PotionEffect> potionEffects = new ArrayList<>(mc.thePlayer.getActivePotionEffects());
        potionEffects = filterEffects(potionEffects);

        if (potionEffects.isEmpty()) {
            if (origin == RenderOrigin.GUI) {
                potionEffects.add(new PotionEffect(Potion.damageBoost.id, 50, 4));
                potionEffects.add(new PotionEffect(Potion.moveSpeed.id, 5000, 2));
                PotionEffect effect = new PotionEffect(Potion.saturation.id, 10000000, 0);
                effect.setPotionDurationMax(true);
                potionEffects.add(effect);
                potionEffects.add(new PotionEffect(Potion.absorption.id, 242, 1));
                potionEffects = filterEffects(potionEffects);
            } else {
                return;
            }
        }

        if (sortingMode.get() == SortingMode.ALPHABETICAL) {
            potionEffects.sort(Comparator.comparing(o -> I18n.format(o.getEffectName())));
        } else {
            potionEffects.sort(Comparator.comparingInt(PotionEffect::getDuration));
            Collections.reverse(potionEffects);
        }
        if (verticalAlign.get() == VerticalAlignmentMode.UP) Collections.reverse(potionEffects);

        float yOff = 0;
        float xOff = 0;
        final int yAmt = ICON_SIZE + verticalSpacing.get();

        this.height = (potionEffects.size() * yAmt) - verticalSpacing.get();
        super.render(partialTicks, origin);
        this.width = 10f;

        GlStateManager.pushMatrix();
        GlStateManager.scale(getPosition().getScale(), getPosition().getScale(), 1);
        for (PotionEffect effect : potionEffects) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            if (!potion.shouldRender(effect)) continue;

            float iconX = x;
            iconX /= getPosition().getScale();

            GlStateManager.color(1f, 1f, 1f, 1f);

            if (showIcon.get()) {
                mc.getTextureManager().bindTexture(GuiContainer.inventoryBackground);
                drawTexturedModalRectF(iconX, (y + yOff) / getPosition().getScale(), potion.getStatusIconIndex() % 8 * 18, 198 + potion.getStatusIconIndex() / 8 * 18, 18, 18);
                xOff = (ICON_SIZE + 4) * getPosition().getScale();
                this.width = Math.max(this.width, xOff / getPosition().getScale());
            }

            if (showTitle.get()) {
                StringBuilder titleSb = new StringBuilder();
                if (titleTextBold.get()) titleSb.append(EnumChatFormatting.BOLD);
                if (titleTextItalic.get()) titleSb.append(EnumChatFormatting.ITALIC);
                if (titleTextUnderline.get()) titleSb.append(EnumChatFormatting.UNDERLINE);
                titleSb.append(I18n.format(potion.getName()));
                int amplifier = effect.getAmplifier() + 1;
                if (showAmplifier.get() && (amplifier != 1 || showAmplifierLevelOne.get())) {
                    titleSb.append(" ");
                    if (amplifierText.get() == AmplifierMode.ROMAN) titleSb.append(I18n.format("evergreenhud.enchantment.level." + amplifier));
                    else titleSb.append(amplifier);
                }
                String builtTitle = titleSb.toString();

                int titleWidth = mc.fontRendererObj.getStringWidth(builtTitle);
                width = Math.max(width, (xOff / getPosition().getScale()) + titleWidth);

                float titleX = x + xOff;
                titleX /= getPosition().getScale();

                float titleY = y + yOff;
                if (!showTime.get())
                    titleY += mc.fontRendererObj.FONT_HEIGHT / 2f;
                titleY /= getPosition().getScale();

                GuiUtils.drawString(mc.fontRendererObj, builtTitle,
                        titleX, titleY, titleTextMode.get() == TextElement.TextMode.SHADOW, titleTextMode.get() == TextElement.TextMode.BORDER, titleTextChroma.get(), false, new Color(titleTextR.get(), titleTextG.get(), titleTextB.get()).getRGB());

            }

            if (showTime.get()) {
                StringBuilder timeSb = new StringBuilder();
                if (timeTextItalic.get()) timeSb.append(EnumChatFormatting.ITALIC);
                if (timeTextUnderline.get()) timeSb.append(EnumChatFormatting.UNDERLINE);
                if (effect.getIsPotionDurationMax()) timeSb.append(permanentText.get());
                else timeSb.append(Potion.getDurationString(effect));
                String builtTime = timeSb.toString();

                int timeWidth = mc.fontRendererObj.getStringWidth(builtTime);
                width = Math.max(width, (xOff / getPosition().getScale()) + timeWidth);

                float timeX = x + xOff;
                timeX /= getPosition().getScale();

                float timeY = y + yOff + (mc.fontRendererObj.FONT_HEIGHT * getPosition().getScale()) + 1;
                if (!showTitle.get())
                    timeY -= mc.fontRendererObj.FONT_HEIGHT / 2f;
                timeY /= getPosition().getScale();

                GuiUtils.drawString(mc.fontRendererObj, builtTime,
                        timeX, timeY, timeTextMode.get() == TextElement.TextMode.SHADOW, timeTextMode.get() == TextElement.TextMode.BORDER, timeTextChroma.get(), false, new Color(timeTextR.get(), timeTextG.get(), timeTextB.get()).getRGB());

            }

            yOff += yAmt * getPosition().getScale();
        }
        GlStateManager.popMatrix();
    }

    private List<PotionEffect> filterEffects(List<PotionEffect> potionEffects) {
        return potionEffects.stream().filter(effect -> !((!showSpeed.get() && effect.getPotionID() == Potion.moveSpeed.id)
                || (!showSlowness.get() && effect.getPotionID() == Potion.moveSlowdown.id)
                || (!showHaste.get() && effect.getPotionID() == Potion.digSpeed.id)
                || (!showMiningFatigue.get() && effect.getPotionID() == Potion.digSlowdown.id)
                || (!showStrength.get() && effect.getPotionID() == Potion.damageBoost.id)
                || (!showJumpBoost.get() && effect.getPotionID() == Potion.jump.id)
                || (!showNausea.get() && effect.getPotionID() == Potion.confusion.id)
                || (!showRegeneration.get() && effect.getPotionID() == Potion.regeneration.id)
                || (!showResistance.get() && effect.getPotionID() == Potion.resistance.id)
                || (!showFireResistance.get() && effect.getPotionID() == Potion.fireResistance.id)
                || (!showWaterBreathing.get() && effect.getPotionID() == Potion.waterBreathing.id)
                || (!showInvisibility.get() && effect.getPotionID() == Potion.invisibility.id)
                || (!showBlindness.get() && effect.getPotionID() == Potion.blindness.id)
                || (!showNightVision.get() && effect.getPotionID() == Potion.nightVision.id)
                || (!showHunger.get() && effect.getPotionID() == Potion.hunger.id)
                || (!showWeakness.get() && effect.getPotionID() == Potion.weakness.id)
                || (!showPoison.get() && effect.getPotionID() == Potion.poison.id)
                || (!showWither.get() && effect.getPotionID() == Potion.wither.id)
                || (!showHealthBoost.get() && effect.getPotionID() == Potion.healthBoost.id)
                || (!showAbsorption.get() && effect.getPotionID() == Potion.absorption.id)
                || (!showSaturation.get() && effect.getPotionID() == Potion.saturation.id)

                || (!showPermanent.get() && effect.getIsPotionDurationMax()))).collect(Collectors.toList());
    }

    @Override
    protected float getHitBoxWidth() {
        return this.width;
    }

    @Override
    protected float getHitBoxHeight() {
        return this.height;
    }

    public enum AmplifierMode {
        ROMAN,
        ARABIC
    }

    public enum VerticalAlignmentMode {
        DOWN,
        UP
    }

    public enum SortingMode {
        ALPHABETICAL,
        DURATION
    }

}
