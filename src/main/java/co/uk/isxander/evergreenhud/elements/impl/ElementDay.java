package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.elements.ElementData;

public class ElementDay extends Element {

    @Override
    public void initialise() {

    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Day Counter", "Displays the current day in the world.");
    }

    @Override
    protected String getValue() {
        if (mc.theWorld == null)
            return "Unknown";

        return Long.toString(mc.theWorld.getWorldTime() / 24000L);
    }

    @Override
    public String getDisplayTitle() {
        return "Day";
    }

}
