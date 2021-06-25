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

package co.uk.isxander.evergreenhud.elements.type;

import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.elements.RenderOrigin;
import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.evergreenhud.settings.impl.FloatSetting;
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.xanderlib.utils.HitBox2D;
import co.uk.isxander.xanderlib.utils.Resolution;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import net.apolloclient.utils.GLRenderer;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public abstract class BackgroundElement extends Element {

    protected FloatSetting paddingLeft;
    protected FloatSetting paddingRight;
    protected FloatSetting paddingTop;
    protected FloatSetting paddingBottom;
    protected IntegerSetting cornerRadius;

    protected BooleanSetting backEnabled;
    protected IntegerSetting backR;
    protected IntegerSetting backG;
    protected IntegerSetting backB;
    protected IntegerSetting backA;

    protected BooleanSetting outlineEnabled;
    protected IntegerSetting outlineR;
    protected IntegerSetting outlineG;
    protected IntegerSetting outlineB;
    protected IntegerSetting outlineA;
    protected FloatSetting outlineThickness;

    @Override
    protected void registerDefaultSettings() {
        super.registerDefaultSettings();

        addSettings(backEnabled = new BooleanSetting("BG Enabled", "Background", "If the background is rendered.", true));
        addSettings(backR = new IntegerSetting("BG Red", "Background", "How much red is in the color of the background.", 0, 0, 255, ""));
        addSettings(backG = new IntegerSetting("BG Green", "Background", "How much green is in the color of the background.", 0, 0, 255, ""));
        addSettings(backB = new IntegerSetting("BG Blue", "Background", "How much blue is in the color of the background.", 0, 0, 255, ""));
        addSettings(backA = new IntegerSetting("BG Alpha", "Background", "How much alpha is in the color of the background.", 100, 0, 255, ""));

        addSettings(paddingLeft = new FloatSetting("Padding Left", "Background", "How far the background extends to the left.", 4f, 0f, 12f, " px"));
        addSettings(paddingRight = new FloatSetting("Padding Right", "Background", "How far the background will extend to the right.", 4f, 0f, 12f, " px"));
        addSettings(paddingTop = new FloatSetting("Padding Top", "Background", "How far the background will extend upwards.", 4f, 0f, 12f, " px"));
        addSettings(paddingBottom = new FloatSetting("Padding Bottom", "Background", "How far the background will extend downwards.", 4f, 0f, 12f, " px"));

        addSettings(cornerRadius = new IntegerSetting("Corner Radius", "Background", "How round are the corners of the background.", 0, 0, 6, ""));

        addSettings(outlineEnabled = new BooleanSetting("Outline Enabled", "Background", "If the outline is rendered.", false));
        addSettings(outlineR = new IntegerSetting("Outline Red", "Background", "How much red is in the color of the outline.", 0, 0, 255, ""));
        addSettings(outlineG = new IntegerSetting("Outline Green", "Background", "How much green is in the color of the outline.", 0, 0, 255, ""));
        addSettings(outlineB = new IntegerSetting("Outline Blue", "Background", "How much blue is in the color of the outline.", 0, 0, 255, ""));
        addSettings(outlineA = new IntegerSetting("Outline Alpha", "Background", "How transparent is the outline.", 0, 0, 255, ""));
        addSettings(outlineThickness = new FloatSetting("Outline Weight", "Background", "How thick is the outline.", 1f, 0.5f, 8, ""));
    }

    @Override
    public void render(float partialTicks, int origin) {
        Color bgCol = getBgColor();
        Color outlineCol = new Color(outlineR.get(), outlineG.get(), outlineB.get(), outlineA.get());

        float scale = getPosition().getScale();
        HitBox2D hitbox = calculateHitBox(1, scale);

        if (cornerRadius.get() == 0) {
            if (backEnabled.get() && bgCol.getAlpha() != 0)
                GLRenderer.drawRectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height, bgCol);
            if (outlineEnabled.get() && outlineCol.getAlpha() != 0)
                GLRenderer.drawHollowRectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height, outlineThickness.get(), outlineCol);
        } else {
            if (backEnabled.get() && bgCol.getAlpha() != 0)
                GLRenderer.drawRoundedRectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height, cornerRadius.get(), bgCol);
            if (outlineEnabled.get() && outlineCol.getAlpha() != 0)
                GLRenderer.drawHollowRoundedRectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height, cornerRadius.get(), outlineThickness.get(), outlineCol);
        }
    }

    @Override
    public HitBox2D calculateHitBox(float gl, float sizeScale) {
        ScaledResolution res = Resolution.get();
        float width = getHitBoxWidth() * sizeScale;
        float height = getHitBoxHeight() * sizeScale;

        float top = getPaddingTopSetting().get() * sizeScale;
        float bottom = getPaddingBottomSetting().get() * sizeScale;
        float left = getPaddingLeftSetting().get() * sizeScale;
        float right = getPaddingRightSetting().get() * sizeScale;

        float x = getPosition().getRawX(res) / gl;
        float y = getPosition().getRawY(res) / gl;

        return new HitBox2D(x - left, y - top, width + left + right, height + top + bottom);
    }

    @Override
    public void loadJsonOld(BetterJsonObject root) {
        BetterJsonObject bgColor = new BetterJsonObject(root.get("bgColor").getAsJsonObject());
        backR.set(bgColor.optInt("r"));
        backG.set(bgColor.optInt("g"));
        backB.set(bgColor.optInt("b"));
        backA.set(bgColor.optInt("a"));

        float paddingWidth = bgColor.optFloat("padding_width");
        float paddingHeight = bgColor.optFloat("padding_height");
        paddingLeft.set(paddingWidth);
        paddingRight.set(paddingWidth);
        paddingTop.set(paddingHeight);
        paddingBottom.set(paddingHeight);

        super.loadJsonOld(root);
    }

    public Color getBgColor() {
        return new Color(backR.get(), backG.get(), backB.get(), backA.get());
    }

    public void setBgColor(int r, int g, int b, int a) {
        backR.set(r);
        backG.set(g);
        backB.set(b);
        backA.set(a);
    }

    public FloatSetting getPaddingLeftSetting() {
        return paddingLeft;
    }

    public FloatSetting getPaddingRightSetting() {
        return paddingRight;
    }

    public FloatSetting getPaddingTopSetting() {
        return paddingTop;
    }

    public FloatSetting getPaddingBottomSetting() {
        return paddingBottom;
    }

    public IntegerSetting getCornerRadiusSetting() {
        return cornerRadius;
    }

    protected boolean useCornerRadiusSetting() {
        return true;
    }
}
