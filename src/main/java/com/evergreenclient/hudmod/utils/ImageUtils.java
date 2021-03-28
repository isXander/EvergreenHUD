/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.utils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public static BufferedImage flipVertically(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < newImage.getWidth(); x++) {
            for (int y1 = 0, y2 = newImage.getHeight() - 1; y1 < newImage.getHeight(); y1++, y2--) {
                newImage.setRGB(x, y1, image.getRGB(x, y2));
            }
        }
        return newImage;
    }

    public static BufferedImage flipHorizontally(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < newImage.getHeight(); y++) {
            for (int x1 = 0, x2 = newImage.getWidth() - 1; x1 < newImage.getWidth(); x1++, x2--) {
                newImage.setRGB(x1, y, image.getRGB(x2, y));
            }
        }
        return newImage;
    }

}
