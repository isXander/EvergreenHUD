package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;

public class ElementDay extends SimpleTextElement {

    @Override
    protected ElementData metadata() {
        return new ElementData("Day Counter", "Displays the current day in the world.", "Simple");
    }

    @Override
    protected String getValue() {
        if (mc.theWorld == null)
            return "Unknown";

        return Long.toString(mc.theWorld.getWorldTime() / 24000L);
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Day";
    }

}
