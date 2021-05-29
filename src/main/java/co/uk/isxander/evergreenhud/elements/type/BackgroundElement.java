package co.uk.isxander.evergreenhud.elements.type;

import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.elements.RenderOrigin;
import co.uk.isxander.evergreenhud.settings.impl.FloatSetting;
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.xanderlib.utils.HitBox2D;
import co.uk.isxander.xanderlib.utils.Resolution;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import net.apolloclient.utils.GLRenderer;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public abstract class BackgroundElement extends Element {

    protected FloatSetting paddingWidth;
    protected FloatSetting paddingHeight;
    protected IntegerSetting cornerRadius;
    protected IntegerSetting backR;
    protected IntegerSetting backG;
    protected IntegerSetting backB;
    protected IntegerSetting backA;

    @Override
    protected void registerDefaultSettings() {
        super.registerDefaultSettings();

        addSettings(backR = new IntegerSetting("Background Red", "Color", "How much red is in the color of the background.", 0, 0, 255, ""));
        addSettings(backG = new IntegerSetting("Background Green", "Color", "How much green is in the color of the background.", 0, 0, 255, ""));
        addSettings(backB = new IntegerSetting("Background Blue", "Color", "How much blue is in the color of the background.", 0, 0, 255, ""));
        addSettings(backA = new IntegerSetting("Background Alpha", "Color", "How much alpha is in the color of the background.", 100, 0, 255, ""));

        addSettings(paddingWidth = new FloatSetting("Padding Width", "Background", "How much extra width the background box will have.", 4f, 0f, 12f, ""));
        addSettings(paddingHeight = new FloatSetting("Padding Height", "Background", "How much extra height the background box will have.", 4f, 0f, 12f, ""));

        addSettings(cornerRadius = new IntegerSetting("Corner Radius", "Background", "How round are the corners of the background.", 0, 0, 6, ""));
    }

    @Override
    public void render(float partialTicks, RenderOrigin origin) {
        float scale = getPosition().getScale();
        HitBox2D hitbox = calculateHitBox(1, scale);
        GLRenderer.drawRoundedRectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height, cornerRadius.get(), getBgColor());
    }

    @Override
    public HitBox2D calculateHitBox(float gl, float sizeScale) {
        ScaledResolution res = Resolution.get();
        float width = getHitBoxWidth() * sizeScale;
        float extraWidth = getPaddingWidthSetting().get() * sizeScale;
        float height = getHitBoxHeight() * sizeScale;
        float extraHeight = getPaddingHeightSetting().get() * sizeScale;
        float x = getPosition().getRawX(res) / gl;
        float y = getPosition().getRawY(res) / gl;

        return new HitBox2D(x - extraWidth, y - extraHeight, width + (extraWidth * 2), height + (extraHeight * 2));
    }

    @Override
    public void loadJsonOld(BetterJsonObject root) {
        BetterJsonObject bgColor = new BetterJsonObject(root.get("bgColor").getAsJsonObject());
        backR.set(bgColor.optInt("r"));
        backG.set(bgColor.optInt("g"));
        backB.set(bgColor.optInt("b"));
        backA.set(bgColor.optInt("a"));
        paddingWidth.set(bgColor.optFloat("padding_width"));
        paddingHeight.set(bgColor.optFloat("padding_height"));

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

    public FloatSetting getPaddingWidthSetting() {
        return paddingWidth;
    }

    public FloatSetting getPaddingHeightSetting() {
        return paddingHeight;
    }

    public IntegerSetting getCornerRadiusSetting() {
        return cornerRadius;
    }

    protected boolean useCornerRadiusSetting() {
        return true;
    }
}
