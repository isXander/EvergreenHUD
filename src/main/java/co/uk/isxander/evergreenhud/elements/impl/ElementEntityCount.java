package co.uk.isxander.evergreenhud.elements.impl;

import club.sk1er.mods.core.util.ReflectionUtil;
import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;
import net.minecraft.client.renderer.RenderGlobal;

import java.lang.reflect.Field;

public class ElementEntityCount extends SimpleTextElement {

    private final Field countEntitiesRendered;

    public ElementEntityCount() {
        countEntitiesRendered = ReflectionUtil.findField(RenderGlobal.class, "countEntitiesRendered", "field_72749_I");
        countEntitiesRendered.setAccessible(true);
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("Entity Count", "How many entities are currently being rendered.", "Simple");
    }

    @Override
    protected String getValue() {
        if (mc.renderGlobal == null)
            return "Unknown";

        Integer count = 0;
        try {
            count = (Integer) countEntitiesRendered.get(mc.renderGlobal);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return count.toString();
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "Entities";
    }

}
