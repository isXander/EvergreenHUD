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

package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.elements.type.BackgroundElement;

public class ElementEmptyBox extends BackgroundElement {

    @Override
    public void initialise() {
        getPaddingWidthSetting().setMin(1f);
        getPaddingWidthSetting().setMax(500f);
        getPaddingWidthSetting().setMin(1f);
        getPaddingWidthSetting().setMax(500f);
        setBgColor(0, 0, 0, 255);
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Empty Box", "No text, no anything. Can be used to censor parts of your screen.", "Other");
    }

    @Override
    protected float getHitBoxWidth() {
        return 50f;
    }

    @Override
    protected float getHitBoxHeight() {
        return 50f;
    }
}
