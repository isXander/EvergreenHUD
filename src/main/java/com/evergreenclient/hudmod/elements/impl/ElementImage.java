/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.impl;

import club.sk1er.mods.core.gui.notification.Notifications;
import com.evergreenclient.hudmod.EvergreenHUD;
import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.gui.elements.BetterGuiSlider;
import com.evergreenclient.hudmod.gui.screens.impl.GuiElementConfig;
import com.evergreenclient.hudmod.settings.impl.ArraySetting;
import com.evergreenclient.hudmod.settings.impl.BooleanSetting;
import com.evergreenclient.hudmod.settings.impl.ButtonSetting;
import com.evergreenclient.hudmod.settings.impl.IntegerSetting;
import com.evergreenclient.hudmod.utils.*;
import com.evergreenclient.hudmod.utils.thirdparty.GLRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ElementImage extends Element {

    private static final File imageFile = new File(mc.mcDataDir, "config/evergreenhud/image.png");
    private static final ResourceLocation unknownImage = new ResourceLocation("evergreenhud/textures/unknown.png");
    private static final float imgSize = 60;

    private Dimension imageDimension;
    private final Map<File, ITextureObject> fileTextureMap = new HashMap<>();
    private boolean changed = false;
    private float scaleMod = 1;

    public BooleanSetting mirror;
    public ArraySetting rotation;
    public BooleanSetting autoScale;

    @Override
    public GuiElementConfig getElementConfigGui() {
        return new GuiElementConfig(this) {
            @Override
            public void addButtons() {
                super.addButtons();
                this.buttonList = this.buttonList.stream().filter(b -> b.id != 3).collect(Collectors.toList());
                this.buttonList.add(new BetterGuiSlider(3, right(), getRow(0), 120, 20, "Scale: ", "%", 1, 300, element.getPosition().getScale() * 100f, false, true, this, "Controls the size of the element."));
            }
        };
    }

    @Override
    public boolean useAlignmentSetting() {
        return false;
    }

    @Override
    public boolean useTitleSetting() {
        return false;
    }

    @Override
    public boolean useInvertedSetting() {
        return false;
    }

    @Override
    public boolean useBracketsSetting() {
        return false;
    }

    @Override
    public boolean useTextColorSetting() {
        return false;
    }

    @Override
    public boolean useChromaSetting() {
        return false;
    }

    @Override
    public boolean useShadowSetting() {
        return false;
    }

    @Override
    public void resetSettings() {
        super.resetSettings();
        imageFile.delete();

        try {
            Files.copy(ElementImage.class.getResourceAsStream("/assets/" + unknownImage.getResourceDomain() + "/" + unknownImage.getResourcePath()), imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            BufferedImage img = ImageIO.read(imageFile);
            imageDimension = new Dimension(img.getWidth(), img.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReportedException(CrashReport.makeCrashReport(e, "Failed to copy image."));
        }

    }

    @Override
    public void initialise() {
        try {
            if (!imageFile.exists()) {
                if (!EvergreenHUD.getInstance().isFirstLaunch()) {
                    Notifications.INSTANCE.pushNotification("EvergreenHUD", "It appears EvergreenHUD could not find the image for Custom Image element.\nYou will have to choose your image again.");
                }
                Files.copy(ElementImage.class.getResourceAsStream("/assets/" + unknownImage.getResourceDomain() + "/" + unknownImage.getResourcePath()), imageFile.toPath());
            }

            BufferedImage img = ImageIO.read(imageFile);
            imageDimension = new Dimension(img.getWidth(), img.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReportedException(CrashReport.makeCrashReport(e, "An error was encountered."));
        }

        addSettings(new ButtonSetting("Choose Image", "Picks your desired image to be rendered.", () -> {
            // Add to new thread so the game does not freeze
            new Thread(() -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(mc.mcDataDir, "config/evergreenhud/"));
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image File", "png", "jpg"));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fileChooser.getSelectedFile();
                        BufferedImage image = ImageIO.read(file);
                        ImageIO.write(image, "PNG", imageFile);
                        fileTextureMap.remove(imageFile);
                        imageDimension = new Dimension(image.getWidth(), image.getHeight());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Notifications.INSTANCE.pushNotification("EvergreenHUD", "You have selected a new image.");
                } else {
                    Notifications.INSTANCE.pushNotification("EvergreenHUD", "You must select an image.");
                }
                changed = true;
            }).start();
        }));
        addSettings(mirror = new BooleanSetting("Mirror", "If the image is flipped horizontally.", false) {
            @Override
            protected boolean onChange(boolean oldValue, boolean newValue) {
                changed = true;
                return true;
            }
        });
        addSettings(rotation = new ArraySetting("Rotation", "How will the image be rotated. (Image should be square for best result.)", 0, "0 deg", "90 deg", "180 deg", "270 deg") {
            @Override
            protected boolean onChange(int currentIndex, int newIndex) {
                changed = true;
                return true;
            }
        });
        addSettings(autoScale = new BooleanSetting("Auto Scale", "Automatically scales your image to a constant size depending on the scale.", true));
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Custom Image", "Renders an image of your choosing.");
    }

    @Override
    public void render(RenderGameOverlayEvent event) {
        mc.mcProfiler.startSection(getMetadata().getName());
        GlStateManager.pushMatrix();
        float scale = getPosition().getScale();

        if (autoScale.get()) {
            scaleMod = imgSize / Math.min((float) imageDimension.getWidth(), (float) imageDimension.getHeight());
        } else {
            scaleMod = 1;
        }
        Hitbox hitbox = getHitbox(1, scale);
        GLRenderer.drawRectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height, getBgColor());
        GlStateManager.scale(scale, scale, 1);
        GlStateManager.enableDepth();
        GlStateManager.color(1f, 1f, 1f, 1f);
        bindTexture(imageFile);
        double width = imageDimension.getWidth();
        double height = imageDimension.getHeight();
//            if (rotation.getIndex() == 1 || rotation.getIndex() == 3) {
//                width = imageDimension.getHeight();
//                height = imageDimension.getWidth();
//            }
        double renderWidth = width * scaleMod;
        double renderHeight = height * scaleMod;

        RenderUtils.drawModalRect(this.getPosition().getRawX(event.resolution) / scale, this.getPosition().getRawY(event.resolution) / scale, 0, 0, imageDimension.getWidth(), imageDimension.getHeight(), renderWidth, renderHeight, imageDimension.getWidth(), imageDimension.getHeight());

        GlStateManager.popMatrix();
        mc.mcProfiler.endSection();
    }

    @Override
    public Hitbox getHitbox(float posScale, float sizeScale) {
        ScaledResolution res = new ScaledResolution(mc);

        float width = (float)imageDimension.getWidth() * sizeScale * scaleMod;
        float height = (float)imageDimension.getHeight() * sizeScale * scaleMod;

        float extraWidth = getPaddingWidth() * sizeScale;
        float extraHeight = getPaddingHeight() * sizeScale;
        float x = getPosition().getRawX(res) / posScale;
        float y = getPosition().getRawY(res) / posScale;

        return new Hitbox(x - extraWidth, y - extraHeight, width + (extraWidth * 2), height + (extraHeight * 2));
    }

    private void bindTexture(File file) {
        ITextureObject texture = fileTextureMap.get(file);
        if (texture == null || changed) {
            texture = new ExternalFileTexture(file, this);
            loadTexture(file, texture);
            changed = false;
        }

        GlStateManager.bindTexture(texture.getGlTextureId());
    }

    private void loadTexture(File file, ITextureObject texture) {
        try {
            texture.loadTexture(mc.getResourceManager());
        } catch (IOException e) {
            logger.warn("Failed to load texture: " + file.getPath(), e);
            texture = TextureUtil.missingTexture;
            this.fileTextureMap.put(file, texture);
        } catch (Throwable throwable) {
            ITextureObject finalTexture = texture;
            CrashReport crash = CrashReport.makeCrashReport(throwable, "Registering texture");
            CrashReportCategory crashreportcategory = crash.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", file.getPath());
            crashreportcategory.addCrashSectionCallable("Texture object class", () -> finalTexture.getClass().getName());
            throw new ReportedException(crash);
        }
        this.fileTextureMap.put(file, texture);
    }

    @Override
    protected String getValue() {
        return null;
    }

    @Override
    public String getDisplayTitle() {
        return null;
    }

    public static class ExternalFileTexture extends AbstractTexture {

        protected final File textureLocation;
        protected final ElementImage element;

        public ExternalFileTexture(File file, ElementImage element) {
            this.textureLocation = file;
            this.element = element;
        }

        @Override
        public void loadTexture(IResourceManager resourceManager) throws IOException {
            this.deleteGlTexture();
            InputStream is = new FileInputStream(textureLocation);

            BufferedImage img = TextureUtil.readBufferedImage(is);
            if (element.mirror.get())
                img = ImageUtils.flipHorizontally(img);
            img = ImageUtils.rotate(img, element.rotation.getIndex() * 90);

            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), img, false, false);
            is.close();
        }
    }

}
