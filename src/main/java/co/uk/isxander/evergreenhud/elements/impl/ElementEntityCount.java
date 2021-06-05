package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;

public class ElementEntityCount extends SimpleTextElement {

    @Override
    protected ElementData metadata() {
        return new ElementData("Entity Count", "How many entities are currently being rendered.", "Simple");
    }

    @Override
    protected String getValue() {
        if (mc.renderGlobal == null)
            return "Unknown";

        return Integer.toString(mc.renderGlobal.countEntitiesRendered);
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Entities";
    }

}
