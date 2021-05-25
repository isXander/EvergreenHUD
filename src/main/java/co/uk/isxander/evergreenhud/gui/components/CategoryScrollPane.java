package co.uk.isxander.evergreenhud.gui.components;

import co.uk.isxander.xanderlib.utils.Constants;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;

import java.awt.*;
import java.util.List;

public class CategoryScrollPane extends MinScrollingList implements Constants {

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
    public void elementClicked(int index, boolean doubleClick) {
        selected = index;
        onClick.onClick(categories.get(index), index);
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

    public interface ElementClick {
        void onClick(String category, int id);
    }

}
