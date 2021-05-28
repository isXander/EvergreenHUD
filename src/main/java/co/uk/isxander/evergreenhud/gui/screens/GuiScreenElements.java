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

package co.uk.isxander.evergreenhud.gui.screens;

import co.uk.isxander.evergreenhud.EvergreenHUD;
import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.elements.RenderOrigin;
import co.uk.isxander.evergreenhud.elements.snapping.SnapPoint;
import co.uk.isxander.evergreenhud.gui.screens.impl.GuiElementConfig;
import co.uk.isxander.xanderlib.utils.MathUtils;
import co.uk.isxander.xanderlib.utils.Resolution;
import net.apolloclient.utils.GLRenderer;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GuiScreenElements extends GuiScreenExt {

    protected Element dragging = null;
    protected Element lastClicked = null;
    protected float offX = 0, offY = 0;

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void drawScreen(int mouseXInt, int mouseYInt, float partialTicks) {
        super.drawScreen(mouseXInt, mouseYInt, partialTicks);

        ScaledResolution res = Resolution.get();

        for (Element e : EvergreenHUD.getInstance().getElementManager().getCurrentElements()) {
            e.render(partialTicks, RenderOrigin.GUI);
            e.renderGuiOverlay(lastClicked != null && lastClicked == e);
        }

        float mouseX = ((float)Mouse.getX()) * ((float)this.width) / ((float)this.mc.displayWidth);
        float mouseY = ((float)this.height) - ((float)Mouse.getY()) * ((float)this.height) / ((float)this.mc.displayHeight) - 1f;

        if (dragging != null) {
            float elementX = mouseX - offX;
            float elementY = mouseY - offY;

            if (false) {
                boolean foundPoint = false;
                for (SnapPoint localPoint : dragging.getSnapPoints()) {
                    float localX = localPoint.calculateX(res);
                    float localY = localPoint.calculateY(res);
                    for (Element element : EvergreenHUD.getInstance().getElementManager().getCurrentElements()) {
                        if (dragging == element) continue;

                        for (SnapPoint foreignPoint : element.getSnapPoints()) {
                            float foreignX = foreignPoint.calculateX(res);
                            float foreignY = foreignPoint.calculateY(res);

                            SnapPoint.SnapAxis axis = foreignPoint.calculateSnappingAxis(localPoint, res);

                            if (axis == null) {
                                continue;
                            }

                            if (axis == SnapPoint.SnapAxis.X) {
                                if (elementY > localY + SnapPoint.MAX_SNAP_DIST || elementY < localY - SnapPoint.MAX_SNAP_DIST) {
                                    foundPoint = true;
                                    continue;
                                }
                                elementY = localY + (foreignY - localY);
                            } else {
                                if (elementX > localX + SnapPoint.MAX_SNAP_DIST || elementX < localX - SnapPoint.MAX_SNAP_DIST) {
                                    foundPoint = true;
                                    continue;
                                }
                                elementX = localX + (foreignX - localX);
                            }
                            GLRenderer.drawLine(localX, localY, foreignX, foreignY, 1, new Color(255, 0, 0));

                            foundPoint = true;
                            break;
                        }
                        if (foundPoint) break;
                    }
                    if (foundPoint) break;
                }
            }

            dragging.getPosition().setRawX(elementX, res);
            dragging.getPosition().setRawY(elementY, res);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        ScaledResolution res = Resolution.get();
        boolean clickedElement = false;

        float x = ((float)Mouse.getX()) * ((float)this.width) / ((float)this.mc.displayWidth);
        float y = ((float)this.height) - ((float)Mouse.getY()) * ((float)this.height) / ((float)this.mc.displayHeight) - 1f;

        List<Element> reversed = new ArrayList<>(EvergreenHUD.getInstance().getElementManager().getCurrentElements());
        Collections.reverse(reversed);
        for (Element e : reversed) {
            e.onMouseClicked(mouseX, mouseY);
            if (e.calculateHitBox(1, e.getPosition().getScale()).doesPositionOverlap(mouseX, mouseY)) {
                lastClicked = dragging = e;
                offX = x - e.getPosition().getRawX(res);
                offY = y - e.getPosition().getRawY(res);
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
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        ScaledResolution res = Resolution.get();
        if (lastClicked != null) {
            switch (keyCode) {
                case Keyboard.KEY_UP:
                    lastClicked.getPosition().setRawY(MathUtils.clamp(lastClicked.getPosition().getRawY(res) - 2, 0, res.getScaledHeight() - 1), res);
                    break;
                case Keyboard.KEY_DOWN:
                    lastClicked.getPosition().setRawY(MathUtils.clamp(lastClicked.getPosition().getRawY(res) + 2, 0, res.getScaledHeight() - 1), res);
                    break;
                case Keyboard.KEY_LEFT:
                    lastClicked.getPosition().setRawX(MathUtils.clamp(lastClicked.getPosition().getRawX(res) - 2, 0, res.getScaledWidth() - 1), res);
                    break;
                case Keyboard.KEY_RIGHT:
                    lastClicked.getPosition().setRawX(MathUtils.clamp(lastClicked.getPosition().getRawX(res) + 2, 0, res.getScaledWidth() - 1), res);
                    break;
                case Keyboard.KEY_RETURN:
                    if (this instanceof GuiElementConfig) {
                        GuiElementConfig elementConfig = (GuiElementConfig) this;
                        if (elementConfig.element == lastClicked) {
                            break;
                        }
                    }
                    mc.displayGuiScreen(lastClicked.getElementConfigGui());
                    break;
                case Keyboard.KEY_DELETE:
                case Keyboard.KEY_BACK:
                    EvergreenHUD.getInstance().getElementManager().removeElement(lastClicked);
                    break;
            }
        }

    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        EvergreenHUD.getInstance().getElementManager().getElementConfig().save();
    }

}
