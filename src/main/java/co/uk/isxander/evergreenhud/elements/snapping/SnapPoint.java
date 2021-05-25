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

package co.uk.isxander.evergreenhud.elements.snapping;

import co.uk.isxander.evergreenhud.elements.Element;
import net.minecraft.client.gui.ScaledResolution;

public class SnapPoint {

    public static final int MAX_SNAP_DIST = 5;

    private final Element element;

    private float snapX;
    private float snapY;

    public SnapPoint(Element element, float snapX, float snapY) {
        this.element = element;
        this.snapX = snapX;
        this.snapY = snapY;
    }

    public SnapAxis calculateSnappingAxis(SnapPoint point, ScaledResolution res) {
        return calculateSnappingAxis(point.calculateX(res), point.calculateY(res), res);
    }

    public SnapAxis calculateSnappingAxis(float x, float y, ScaledResolution res) {
        if (calculateX(res) - x < MAX_SNAP_DIST) {
            return SnapAxis.X;
        } else if (calculateY(res) - y < MAX_SNAP_DIST) {
            return SnapAxis.Y;
        }
        return null;
    }

    public float calculateX(float x) {
        return x + (element.calculateHitBox(1, element.getPosition().getScale()).width * snapX);
    }

    public float calculateX(ScaledResolution res) {
        return calculateX(element.getPosition().getRawX(res));
    }

    public float calculateY(float y) {
        return y + (element.calculateHitBox(1, element.getPosition().getScale()).height * snapY);
    }

    public float calculateY(ScaledResolution res) {
        return calculateY(element.getPosition().getRawY(res));
    }

    public float getSnapX() {
        return snapX;
    }

    public void setSnapX(float snapX) {
        this.snapX = snapX;
    }

    public float getSnapY() {
        return snapY;
    }

    public void setSnapY(float snapY) {
        this.snapY = snapY;
    }

    public enum SnapAxis {
        X, Y
    }
}
