/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.gui.screens.impl.GuiElementConfig;
import com.evergreenclient.hudmod.settings.Setting;
import com.evergreenclient.hudmod.settings.impl.BooleanSetting;
import com.evergreenclient.hudmod.utils.Alignment;
import com.evergreenclient.hudmod.utils.ElementData;
import com.evergreenclient.hudmod.utils.Hitbox;
import com.evergreenclient.hudmod.utils.thirdparty.GLRenderer;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ElementScoreboard extends Element {

    public BooleanSetting showNumbers;

    private float x = 0;
    private float y = 0;
    private float scoreWidth = 0;
    private float scoreHeight = 0;

    @Override
    public void initialise() {
        GuiIngameForge.renderObjective = false;

        addSettings(showNumbers = new BooleanSetting("Show Numbers", "Show the red numbers after the score.", false));
    }

    @Override
    public GuiElementConfig getElementConfigGui() {
        return new GuiElementConfig(this) {
            @Override
            protected void actionPerformed(GuiButton button) {
                super.actionPerformed(button);
                ScaledResolution res = new ScaledResolution(mc);
                if (button.id == 16) {
                    switch (getAlignment()) {
                        case CENTER:
                            setAlignment(Alignment.RIGHT);
                            addButtons();
                            getPosition().setRawX(getPosition().getRawX(res) - (scoreWidth / 2f) - (scoreWidth / 4f) - (scoreWidth / 8f), res);
                            break;
                        case RIGHT:
                            getPosition().setRawX(getPosition().getRawX(res) - (scoreWidth / 2f) - (scoreWidth / 4f) - (scoreWidth / 8f), res);
                            break;
                        case LEFT:
                            getPosition().setRawX(getPosition().getRawX(res) + (scoreWidth / 2f) + (scoreWidth / 4f) + (scoreWidth / 8f), res);
                            break;
                    }
                }
            }
        };
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Scoreboard", "Renders the vanilla scoreboard.");
    }

    @Override
    public void render(RenderGameOverlayEvent event) {
        ScaledResolution res = event.resolution;
        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        ScoreObjective objective = null;
        ScorePlayerTeam team = scoreboard.getPlayersTeam(mc.thePlayer.getName());
        if (team != null) {
            int slot = team.getChatFormat().getColorIndex();
            if (slot >= 0)
                objective = scoreboard.getObjectiveInDisplaySlot(3 + slot);
        }
        ScoreObjective o = objective != null ? objective : scoreboard.getObjectiveInDisplaySlot(1);
        if (o != null) {
            renderScoreboard(o, res);
        }
    }

    private void renderScoreboard(ScoreObjective o, ScaledResolution res) {
        Scoreboard scoreboard = o.getScoreboard();

        Collection<Score> sortedScores = scoreboard.getSortedScores(o);
        List<Score> filteredScores = sortedScores.stream().filter(score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#")).collect(Collectors.toList());

        if (filteredScores.size() > 15) {
            sortedScores = Lists.newArrayList(Iterables.skip(filteredScores, sortedScores.size() - 15));
        }
        else {
            sortedScores = filteredScores;
        }

        int maxStrWidth = mc.fontRendererObj.getStringWidth(o.getDisplayName());

        for (Score score : sortedScores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()) + (showNumbers.get() ? ": " + EnumChatFormatting.RED + score.getScorePoints() + " " : " ");
            maxStrWidth = Math.max(maxStrWidth, mc.fontRendererObj.getStringWidth(s));
        }
        maxStrWidth += 1;
        scoreWidth = maxStrWidth;
        scoreHeight = (sortedScores.size() + 1) * mc.fontRendererObj.FONT_HEIGHT;

        float y = (getPosition().getRawY(res) / getPosition().getScale()) + (sortedScores.size() * mc.fontRendererObj.FONT_HEIGHT) / 3f;
        float x = (getPosition().getRawX(res) - maxStrWidth / getPosition().getScale());

        float lineRight = x + maxStrWidth;
        this.x = x;
        this.y = y;

        int i = 0;
        GlStateManager.pushMatrix();
        GlStateManager.scale(getPosition().getScale(), getPosition().getScale(), 1);
        for (Score s : sortedScores) {
            ++i;

            ScorePlayerTeam team = scoreboard.getPlayersTeam(s.getPlayerName());
            String name = ScorePlayerTeam.formatPlayerName(team, s.getPlayerName());
            String points = (showNumbers.get() ? EnumChatFormatting.RED + "" + s.getScorePoints() : "");
            float lineY = y - i * mc.fontRendererObj.FONT_HEIGHT;
            GLRenderer.drawRectangle(x - 2, lineY, maxStrWidth, mc.fontRendererObj.FONT_HEIGHT, getBgColor());
            mc.fontRendererObj.drawString(name, x, lineY, 553648127, renderShadow());
            mc.fontRendererObj.drawString(points, lineRight - mc.fontRendererObj.getStringWidth(points) - 3, lineY, 553648127, renderShadow());

            if (i == sortedScores.size()) {
                String title = o.getDisplayName();
                // Title Rect
                GLRenderer.drawRectangle(x - 2, lineY - mc.fontRendererObj.FONT_HEIGHT - 1, maxStrWidth, mc.fontRendererObj.FONT_HEIGHT, getTextColor());
                //drawRect(x - 2, lineY - mc.fontRendererObj.FONT_HEIGHT - 1, lineRight, lineY - 1, 1610612736);
                // Part of the value rect
                GLRenderer.drawRectangle(x - 2, lineY - 1, maxStrWidth, 1, getBgColor());
                //drawRect(x - 2, lineY - 1, lineRight, lineY, 1342177280);
                mc.fontRendererObj.drawString(title, x + maxStrWidth / 2f - mc.fontRendererObj.getStringWidth(title) / 2f, lineY - mc.fontRendererObj.FONT_HEIGHT, 553648127, renderShadow());
            }
        }

        GlStateManager.popMatrix();
    }

    @Override
    public void resetSettings() {
        setEnabled(true);
        getPosition().setScaledY(0.5f);
        getPosition().setScaledX(1f);
        setTitle(false);
        setBrackets(false);
        setInverted(false);
        setShadow(true);
        setAlignment(Alignment.LEFT);
        setTextColor(new Color(0, 0, 0, 96));
        setBgColor(new Color(0, 0, 0, 80));
        setPaddingWidth(4);
        setPaddingHeight(4);
        for (Setting s : getCustomSettings())
            s.reset();
        getConfig().save();
    }

    @Override
    public Hitbox getHitbox(float posScale, float sizeScale) {
        float width = scoreWidth * sizeScale;
        float height = scoreHeight + 3 * sizeScale;
        float hitX = x - 2 / posScale;
        float hitY = y + 1 - height / posScale;
        return new Hitbox(hitX, hitY, width, height);
    }

    @Override
    protected String getValue() {
        return null;
    }

    @Override
    public String getDisplayTitle() {
        return null;
    }

}
