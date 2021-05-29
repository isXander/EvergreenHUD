package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.elements.type.BackgroundElement;

public class ElementPotionHUD extends BackgroundElement {



    @Override
    protected ElementData metadata() {
        return new ElementData("PotionHUD", "Displays information about your current potion effects.", "Advanced");
    }

}
