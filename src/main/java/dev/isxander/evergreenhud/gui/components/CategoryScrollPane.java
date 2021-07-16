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

package dev.isxander.evergreenhud.gui.components;

import static dev.isxander.xanderlib.utils.Constants.*;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;

import java.awt.*;
import java.util.List;

public class CategoryScrollPane extends MinScrollingList {

    private final List<String> categories;
    private final ElementClick onClick;
    private int selected;

    public CategoryScrollPane(int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight, List<String> categories, ElementClick onClick) {
        super(mc, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);

        this.categories = categories;
        this.onClick = onClick;
        this.selected = 0;
    }

    @Override
    protected int getSize() {
        return this.categories.size();
    }

    @Override
    public boolean elementClicked(int index, boolean doubleClick) {
        if (categories.get(index).trim().equalsIgnoreCase("")) return false;

        selected = index;
        onClick.onClick(categories.get(index), index);

        return true;
    }

    @Override
    public void setIndex(int index) {
        super.setIndex(index);
        elementClicked(index, false);
    }

    @Override
    protected boolean isSelected(int index) {
        return selected == index;
    }

    @Override
    protected void drawBackground() {
        Gui.drawRect(this.left, this.top, this.right, this.bottom, new Color(0, 0, 0, 100).getRGB());
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        if (isSelected(slotIdx))
            Gui.drawRect(this.left, slotTop, this.right, slotTop + slotHeight, new Color(0, 255, 0, 30).getRGB());
        float y = slotTop + (this.slotHeight / 2f) - (mc.fontRendererObj.FONT_HEIGHT / 2f);
        mc.fontRendererObj.drawString(this.categories.get(slotIdx), this.left + 3, y, -1, true);
    }

    public List<String> getCategories() {
        return categories;
    }

    public interface ElementClick {
        void onClick(String category, int id);
    }

}
