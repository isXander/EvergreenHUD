/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.impl;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.utils.ElementData;
import com.evergreenclient.hudmod.utils.Hitbox;
import com.evergreenclient.hudmod.utils.thirdparty.GLRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class ElementPlayerPreview extends Element {

    @Override
    public void initialise() {

    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Player Preview", "Renders the player like in your inventory.");
    }

    @Override
    protected String getValue() {
        return null;
    }

    @Override
    public boolean useShadowSetting() {
        return false;
    }

    @Override
    public boolean useChromaSetting() {
        return false;
    }

    @Override
    public boolean useTitleSetting() {
        return false;
    }

    @Override
    public boolean useAlignmentSetting() {
        return false;
    }

    @Override
    public boolean useTextColorSetting() {
        return false;
    }

    @Override
    public boolean useBracketsSetting() {
        return false;
    }

    @Override
    public boolean useInvertedSetting() {
        return false;
    }

    @Override
    public void render(RenderGameOverlayEvent event) {
        GlStateManager.pushMatrix();
        GlStateManager.enableDepth();
        Hitbox hitbox = getHitbox(1, getPosition().getScale());
        GLRenderer.drawRectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height, getBgColor());

        EntityPlayerSP ent = mc.thePlayer;
        float posX = getPosition().getRawX(event.resolution);
        float posY = getPosition().getRawY(event.resolution);
        float scale = getPosition().getScale() * 50;

        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, 50.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        ent.renderYawOffset = 0;
        ent.rotationYaw = 0;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.popMatrix();
    }

    @Override
    public Hitbox getHitbox(float posScale, float sizeScale) {
        ScaledResolution res = new ScaledResolution(mc);

        float width = 80 * sizeScale;
        float extraWidth = getPaddingWidth() * sizeScale;
        float height = 120 * sizeScale;
        float extraHeight = getPaddingHeight() * sizeScale;
        float x = getPosition().getRawX(res) - (width / 2f) / posScale;
        float y = getPosition().getRawY(res) - height + (height / 8f) + (height / 128f) / posScale;

        return new Hitbox(x - extraWidth, y - extraHeight, width + (extraWidth * 2), height + (extraHeight * 2));
    }

    @Override
    public String getDisplayTitle() {
        return null;
    }
}
