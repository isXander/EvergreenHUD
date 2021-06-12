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

import club.sk1er.mods.core.gui.notification.Notifications;
import co.uk.isxander.evergreenhud.elements.RenderOrigin;
import co.uk.isxander.evergreenhud.elements.type.BackgroundElement;
import co.uk.isxander.evergreenhud.settings.impl.*;
import co.uk.isxander.xanderlib.utils.HitBox2D;
import co.uk.isxander.xanderlib.utils.ImageUtils;
import co.uk.isxander.evergreenhud.EvergreenHUD;
import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.xanderlib.utils.Resolution;
import net.apolloclient.utils.GLRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ElementImage extends BackgroundElement {

    private static final String textureName = "EVERGREEN_IMAGE_ELEMENT";
    private static final File imageFile = new File(EvergreenHUD.DATA_DIR, "image.png");
    private static final ResourceLocation unknownImage = new ResourceLocation("evergreenhud", "textures/unknown.png");
    private static final float imgSize = 60;

    private Dimension imageDimension;
    private ResourceLocation currentImage;
    private boolean changed = false;
    private float scaleMod = 1;

    public StringSetting fileLocation;
    public BooleanSetting mirror;
    public FloatSetting rotation;
    public BooleanSetting autoScale;

    @Override
    public void resetSettings(boolean save) {
        super.resetSettings(save);

        copyNullImage();
    }

    @Override
    public void initialise() {
        addSettings(fileLocation = new StringSetting("File Path", "", "", true));

        addSettings(new ButtonSetting("Choose Image", "Display", "Picks your desired image to be rendered.", () -> {
            // Add to new thread so the game does not freeze
            new Thread(() -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(EvergreenHUD.DATA_DIR);
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image File", "png", "jpg"));
                Notifications.INSTANCE.pushNotification("EvergreenHUD", "The file dialogue has just been opened. You may need to tab out to see it.");
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    fileLocation.set(file.getPath());

                    changed = true;
                    Notifications.INSTANCE.pushNotification("EvergreenHUD", "You have selected a new image.");
                } else {
                    Notifications.INSTANCE.pushNotification("EvergreenHUD", "You must select an image.");
                }
            }).start();
        }));
        addSettings(mirror = new BooleanSetting("Mirror", "Image", "If the image is flipped horizontally.", false) {
            @Override
            protected boolean onChange(boolean oldValue, boolean newValue) {
                changed = true;
                return true;
            }
        });
        addSettings(rotation = new FloatSetting("Rotation", "Image", "How the image will be rotated.", 0, 0, 360, " deg"));
        addSettings(autoScale = new BooleanSetting("Auto Scale", "Image", "Automatically scales your image to a constant size depending on the scale.", true));
    }

    @Override
    protected void onSettingsLoad() {
        try {
            cacheResourceLocation();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReportedException(CrashReport.makeCrashReport(e, "An error was encountered."));
        }
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Custom Image", "Renders an image of your choosing.", "Advanced");
    }

    @Override
    public void render(float partialTicks, RenderOrigin origin) {
        if (changed || currentImage == null) {
            // Reload the texture
            if (currentImage != null)
                mc.getTextureManager().deleteTexture(currentImage);
            try {
                cacheResourceLocation();
            } catch (IOException e) {
                e.printStackTrace();
            }

            changed = false;
        }

        GlStateManager.pushMatrix();
        float scale = getPosition().getScale();

        if (autoScale.get()) {
            scaleMod = imgSize / Math.min((float) imageDimension.getWidth(), (float) imageDimension.getHeight());
        } else {
            scaleMod = 1;
        }
        GlStateManager.pushMatrix();
        HitBox2D hitbox = calculateHitBox(1, getPosition().getScale());
        float bgPivotX = hitbox.x + (hitbox.width / 2f);
        float bgPivotY = hitbox.y + (hitbox.height / 2f);
        GlStateManager.translate(bgPivotX, bgPivotY, 0);
        GlStateManager.rotate(rotation.get(), 0, 0, 1);
        GlStateManager.translate(-bgPivotX, -bgPivotY, 0);
        super.render(partialTicks, origin);
        GlStateManager.popMatrix();
        GlStateManager.scale(scale, scale, 1);
        GlStateManager.enableDepth();
        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(currentImage);
        double width = imageDimension.getWidth();
        double height = imageDimension.getHeight();
        double renderWidth = width * scaleMod;
        double renderHeight = height * scaleMod;
        float renderX = this.getPosition().getRawX(Resolution.get());
        float renderY = this.getPosition().getRawY(Resolution.get());

        float imgPivotX = (renderX + ((float) renderWidth / 2f)) / scale;
        float imgPivotY = (renderY + ((float) renderHeight / 2f)) / scale;
        GlStateManager.translate(imgPivotX, imgPivotY, 0);
        GlStateManager.rotate(rotation.get(), 0, 0, 1);
        GlStateManager.translate(-imgPivotX, -imgPivotY, 0);

        GLRenderer.drawModalRect(renderX / scale, renderY / scale, 0, 0, imageDimension.getWidth(), imageDimension.getHeight(), renderWidth, renderHeight, imageDimension.getWidth(), imageDimension.getHeight());

        GlStateManager.popMatrix();
    }

    @Override
    public HitBox2D calculateHitBox(float gl, float sizeScale) {
        ScaledResolution res = Resolution.get();

        float width = (float)imageDimension.getWidth() * sizeScale * scaleMod;
        float height = (float)imageDimension.getHeight() * sizeScale * scaleMod;

        float extraWidth = getPaddingWidthSetting().get() * sizeScale;
        float extraHeight = getPaddingHeightSetting().get() * sizeScale;
        float x = getPosition().getRawX(res) / gl;
        float y = getPosition().getRawY(res) / gl;

        return new HitBox2D(x - extraWidth, y - extraHeight, width + (extraWidth * 2), height + (extraHeight * 2));
    }

    private void cacheResourceLocation() throws IOException {
        BufferedImage img = ImageIO.read(getImageFile());

        if (this.mirror.get())
            img = ImageUtils.mirror(img);

        currentImage = mc.getTextureManager().getDynamicTextureLocation(textureName, new DynamicTexture(img));
        imageDimension = new Dimension(img.getWidth(), img.getHeight());
    }

    private File getImageFile() {
        if (fileLocation.get().equals("") || !new File(fileLocation.get()).exists()) {
            if (!imageFile.exists()) {
                copyNullImage();
            }
            fileLocation.set(imageFile.getPath());
        }

        return new File(fileLocation.get());
    }

    private void copyNullImage() {
        try {
            Files.copy(ElementImage.class.getResourceAsStream("/assets/" + unknownImage.getResourceDomain() + "/" + unknownImage.getResourcePath()), imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            BufferedImage img = ImageIO.read(imageFile);
            imageDimension = new Dimension(img.getWidth(), img.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReportedException(CrashReport.makeCrashReport(e, "Failed to copy image."));
        }
    }

}
