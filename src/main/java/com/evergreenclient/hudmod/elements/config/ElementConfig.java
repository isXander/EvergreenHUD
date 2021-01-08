/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.elements.config;

import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.utils.json.BetterJsonObject;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.File;

public class ElementConfig {

    private final Element element;
    public final File configFile;



    public ElementConfig(Element e) {
        this.element = e;
        this.configFile = new File(Minecraft.getMinecraft().mcDataDir, "config/evergreenhud/" + element.getMetadata().getName() + ".json");
    }

    public void save() {
        BetterJsonObject root = new BetterJsonObject();
        root.addProperty("enabled", element.isEnabled());
        root.addProperty("x", element.getPosition().x);
        root.addProperty("y", element.getPosition().y);
        root.addProperty("scale", element.getPosition().scale);
        root.addProperty("prefix", element.showPrefix());
        root.addProperty("brackets", element.showBrackets());
        root.addProperty("color", element.getTextColor().getRGB());
        root.addProperty("shadow", element.renderShadow());
        root.addProperty("alignment", element.getAlignment().ordinal());
        BetterJsonObject custom = new BetterJsonObject();
        root.add("custom", custom);
        root.writeToFile(configFile);
    }

    public void load() {
        BetterJsonObject root;
        try {
            root = BetterJsonObject.getFromFile(configFile);
        } catch (Exception e) {
            save();
            return;
        }
        element.setEnabled(root.optBoolean("enabled"));
        element.getPosition().x = root.optInt("x");
        element.getPosition().y = root.optInt("y");
        element.getPosition().scale = root.optInt("scale");
        element.setPrefix(root.optBoolean("prefix"));
        element.setBrackets(root.optBoolean("brackets"));
        element.setTextColor(new Color(root.optInt("color")));
        element.setShadow(root.optBoolean("shadow"));
        element.setAlignment(Element.Alignment.values()[root.optInt("alignment")]);
    }

}
