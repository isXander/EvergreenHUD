/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/Evergreen-Client/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package com.evergreenclient.hudmod.gui.screens;

import co.uk.isxander.xanderlib.utils.HitBox2D;
import co.uk.isxander.xanderlib.utils.MathUtils;
import com.evergreenclient.hudmod.EvergreenHUD;
import com.evergreenclient.hudmod.elements.Element;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class GuiScreenElements extends GuiScreenExt {

    protected Element dragging = null;
    protected Element lastClicked = null;
    protected Map<Element, Map.Entry<Float, Float>> movables = new HashMap<>();
    protected float offX = 0, offY = 0;
    private final DecimalFormat df;

    public GuiScreenElements() {
        df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.FLOOR);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        ScaledResolution res = new ScaledResolution(mc);

        for (Element e : EvergreenHUD.getInstance().getElementManager().getCurrentElements()) {
            e.render(new RenderGameOverlayEvent(partialTicks, res));
            e.renderGuiOverlay(lastClicked != null && lastClicked == e);
        }

        float x = ((float)Mouse.getEventX()) * ((float)this.width) / ((float)this.mc.displayWidth);
        float y = ((float)this.height) - ((float)Mouse.getEventY()) * ((float)this.height) / ((float)this.mc.displayHeight) - 1f;

        if (dragging != null) {
            movables.put(dragging, new AbstractMap.SimpleEntry<>(
                    MathUtils.getPercent(x - offX, 0, res.getScaledWidth()),
                    MathUtils.getPercent(y - offY, 0, res.getScaledHeight())));
        }
        List<Element> toRemove = new ArrayList<>();
        movables.forEach((e, p) -> {
            e.getPosition().setScaledX(MathUtils.lerp(e.getPosition().getXScaled(), p.getKey(), partialTicks * 3));
            e.getPosition().setScaledY(MathUtils.lerp(e.getPosition().getYScaled(), p.getValue(), partialTicks * 3));

            // Remove element once it is done moving
            if (dragging != e && MathUtils.precision(e.getPosition().getXScaled(), 4) == MathUtils.precision(p.getKey(), 4) && MathUtils.precision(e.getPosition().getYScaled(), 4) == MathUtils.precision(p.getValue(), 4)) {
                toRemove.add(e);
            }
        });
        toRemove.forEach((e) -> movables.remove(e));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        ScaledResolution res = new ScaledResolution(mc);
        boolean clickedElement = false;
        for (Element e : EvergreenHUD.getInstance().getElementManager().getCurrentElements()) {
            e.onMouseClicked(mouseX, mouseY);
            if (e.getHitbox(1, e.getPosition().getScale()).doesPositionOverlap(mouseX, mouseY)) {
                lastClicked = dragging = e;
                offX = mouseX - e.getPosition().getRawX(res);
                offY = mouseY - e.getPosition().getRawY(res);
                clickedElement = true;
                break;
            }
        }

        if (!clickedElement) {
            lastClicked = null;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = null;
        offX = offY = 0;
    }

    @Override
    public void onGuiClosed() {
        EvergreenHUD.getInstance().getElementManager().getElementConfig().save();
    }

}
