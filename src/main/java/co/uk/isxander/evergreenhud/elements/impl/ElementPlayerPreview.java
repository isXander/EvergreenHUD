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
import co.uk.isxander.evergreenhud.settings.impl.FloatSetting;
import co.uk.isxander.xanderlib.utils.HitBox2D;
import co.uk.isxander.xanderlib.utils.Resolution;
import net.apolloclient.utils.GLRenderer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;

import java.awt.*;

public class ElementPlayerPreview extends BackgroundElement {

    public FloatSetting rotation;

    @Override
    public void initialise() {
        addSettings(rotation = new FloatSetting("Rotation", "Render", "The rotation of the player.", 0, 0, 360, " deg"));
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Player Preview", "Renders the player like in your inventory.", "Advanced");
    }

    @Override
    public void render(float partialTicks, RenderOrigin origin) {
        GlStateManager.pushMatrix();
        GlStateManager.enableDepth();
        super.render(partialTicks, origin);
        if (mc.thePlayer == null) {
            GlStateManager.disableDepth();
            GlStateManager.popMatrix();
            return;
        }

        EntityPlayerSP ent = mc.thePlayer;
        float posX = getPosition().getRawX(Resolution.get());
        float posY = getPosition().getRawY(Resolution.get());
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
        float rotation = 360f - this.rotation.get();
        ent.renderYawOffset = rotation;
        ent.rotationYaw = rotation;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = mc.getRenderManager();
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

        //GLRenderer.drawRectangle(posX - 1, posY - 1, 2, 2, new Color(255, 0, 0));
    }

    @Override
    public HitBox2D calculateHitBox(float gl, float sizeScale) {
        ScaledResolution res = Resolution.get();

        float width = getHitBoxWidth() * sizeScale;
        float extraWidth = getPaddingWidthSetting().get() * sizeScale;
        float height = getHitBoxHeight() * sizeScale;
        float extraHeight = getPaddingHeightSetting().get() * sizeScale;
        float x = getPosition().getRawX(res) - (width / 2f) / gl;
        float y = getPosition().getRawY(res) - height + (height / 8f) + (height / 128f) / gl;

        return new HitBox2D(x - extraWidth, y - extraHeight, width + (extraWidth * 2), height + (extraHeight * 2));
    }

    @Override
    protected float getHitBoxWidth() {
        return 80f;
    }

    @Override
    protected float getHitBoxHeight() {
        return 120f;
    }
}
