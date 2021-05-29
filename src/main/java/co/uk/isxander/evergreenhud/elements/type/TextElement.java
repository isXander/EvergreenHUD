package co.uk.isxander.evergreenhud.elements.type;

import co.uk.isxander.evergreenhud.settings.impl.BooleanSetting;
import co.uk.isxander.evergreenhud.settings.impl.EnumSetting;
import co.uk.isxander.evergreenhud.settings.impl.IntegerSetting;
import co.uk.isxander.evergreenhud.settings.impl.StringSetting;
import co.uk.isxander.xanderlib.utils.HitBox2D;
import co.uk.isxander.xanderlib.utils.Resolution;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public abstract class TextElement extends BackgroundElement {

    private StringSetting titleText;
    private BooleanSetting brackets;
    private EnumSetting<TextMode> textMode;
    private BooleanSetting chroma;
    private EnumSetting<Alignment> alignment;
    private IntegerSetting textR;
    private IntegerSetting textG;
    private IntegerSetting textB;

    /**
     * @return the default display title
     */
    public abstract String getDefaultDisplayTitle();

    /**
     * Gets the box around the text
     *
     * @param gl the gl scale
     * @param sizeScale the modified scale
     * @return hitbox for rendering & gui
     */
    @Override
    public HitBox2D calculateHitBox(float gl, float sizeScale) {
        HitBox2D hitbox = null;
        ScaledResolution res = Resolution.get();
        float width = getHitBoxWidth() * sizeScale;
        float extraWidth = getPaddingWidthSetting().get() * sizeScale;
        float height = getHitBoxHeight() * sizeScale;
        float extraHeight = getPaddingHeightSetting().get() * sizeScale;
        float x = getPosition().getRawX(res) / gl;
        float y = getPosition().getRawY(res) / gl;
        switch (getAlignmentSetting().get()) {
            case RIGHT:
                hitbox = new HitBox2D(x - (width / sizeScale) - extraWidth, y - extraHeight, width + (extraWidth * 2), height + (extraHeight * 2));
                break;
            case CENTER:
                hitbox = new HitBox2D(x - (width / 2f) - extraWidth, y - extraHeight, width + (extraWidth * 2), height + (extraHeight * 2));
                break;
            case LEFT:
                hitbox = new HitBox2D(x - extraWidth, y - extraHeight, width + (extraWidth * 2), height + (extraHeight * 2));
                break;
        }
        return hitbox;
    }

    @Override
    protected void registerDefaultSettings() {
        super.registerDefaultSettings();

        addSettings(brackets = new BooleanSetting("Brackets", "Display", "If there are square brackets before and after the text.", false));
        addSettings(titleText = new StringSetting("Title", "Display", "What is displayed before or after the value.", getDefaultDisplayTitle()));
        addSettings(textR = new IntegerSetting("Text Red", "Color", "How much red is in the color of the text.", 255, 0, 255, ""));
        addSettings(textG = new IntegerSetting("Text Green", "Color", "How much green is in the color of the text.", 255, 0, 255, ""));
        addSettings(textB = new IntegerSetting("Text Blue", "Color", "How much blue is in the color of the text.", 255, 0, 255, ""));
        addSettings(chroma = new BooleanSetting("Chroma Text", "Color", "If the color of the text is a multicolored mess.", false));
        addSettings(textMode = new EnumSetting<>("Text Mode", "Display", "How should the text be rendered.", TextMode.SHADOW));
        addSettings(alignment = new EnumSetting<>("Alignment", "Display", "When the text grows or shrinks in size, which way the element will move.", Alignment.LEFT));
    }

    @Override
    public void loadJsonOld(BetterJsonObject root) {
        if (!root.optBoolean("title", true)) {
            titleText.set("");
        }
        brackets.set(root.optBoolean("brackets", false));
        chroma.set(root.optBoolean("chroma", false));
        textMode.set((root.optBoolean("shadow", true) ? TextMode.SHADOW : TextMode.NORMAL));

        if (root.has("alignment")) {
            Alignment alignment = Alignment.values()[root.optInt("alignment", 0)];

            if (alignment == Alignment.LEFT) {
                alignment = Alignment.RIGHT;
            } else if (alignment == Alignment.RIGHT) {
                alignment = Alignment.LEFT;
            }

            this.alignment.set(alignment);
        }
        if (root.has("align")) {
            alignment.set(Alignment.values()[root.optInt("align", 0)]);
        }

        BetterJsonObject textColor = new BetterJsonObject(root.get("textColor").getAsJsonObject());
        textR.set(textColor.optInt("r"));
        textG.set(textColor.optInt("g"));
        textB.set(textColor.optInt("b"));

        super.loadJsonOld(root);
    }

    public StringSetting getTitleTextSetting() {
        return titleText;
    }

    public BooleanSetting getBracketsSetting() {
        return brackets;
    }
    public BooleanSetting getChromaSetting() {
        return chroma;
    }

    public Color getTextColor() {
        return new Color(textR.get(), textG.get(), textB.get(), 255);
    }

    public void setTextColor(int r, int g, int b) {
        textR.set(r);
        textG.set(g);
        textB.set(b);
    }

    public EnumSetting<Alignment> getAlignmentSetting() {
        return alignment;
    }

    public EnumSetting<TextMode> getTextModeSetting() {
        return textMode;
    }

    public enum TextMode {
        NORMAL,
        SHADOW,
        BORDER
    }

    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

}
