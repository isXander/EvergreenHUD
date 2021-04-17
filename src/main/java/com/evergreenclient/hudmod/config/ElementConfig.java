/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.config;

import com.evergreenclient.hudmod.EvergreenHUD;
import com.evergreenclient.hudmod.elements.Element;
import com.evergreenclient.hudmod.elements.ElementManager;
import com.evergreenclient.hudmod.elements.ElementType;
import com.evergreenclient.hudmod.elements.impl.ElementImage;
import com.evergreenclient.hudmod.settings.impl.StringSetting;
import com.evergreenclient.hudmod.utils.BetterJsonObject;
import com.google.gson.JsonArray;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ElementConfig {

    public static final int VERSION = 2;
    private static final File oldConfigFolder = new File(Minecraft.getMinecraft().mcDataDir, "config/evergreenhud/elements");

    private final ElementManager manager;
    public final File configFile;

    public ElementConfig(ElementManager manager) {
        this.manager = manager;
        this.configFile = new File(Minecraft.getMinecraft().mcDataDir, "config/evergreenhud/elements.json");
    }

    private BetterJsonObject generateJson() {
        BetterJsonObject root = new BetterJsonObject();
        root.addProperty("version", VERSION);

        JsonArray array = new JsonArray();
        for (Element element : manager.getCurrentElements()) {
            BetterJsonObject obj = new BetterJsonObject();
            obj.addProperty("type", element.getType().name());
            obj.add("settings", element.generateJson());
            array.add(obj.getData());
        }
        root.getData().add("elements", array);

        return root;
    }

    public void save() {
        BetterJsonObject root = generateJson();
        root.writeToFile(configFile);
    }

    public void load() {
        if (!configFile.exists() && oldConfigFolder.exists()) {
            loadOld();

            save();
            return;
        }

        BetterJsonObject root;
        try {
            root = BetterJsonObject.getFromFile(configFile);
        } catch (Exception e) {
            save();
            return;
        }
        // new config version so we need to reset the config
        if (root.optInt("version") != VERSION) {
            manager.getLogger().warn("Resetting configuration! Older or newer config version detected.");
            EvergreenHUD.getInstance().notifyConfigReset();
            save();
            return;
        }

        JsonArray array = root.getData().getAsJsonArray("elements");
        array.forEach((jsonElement) -> {
            BetterJsonObject elementConfig = new BetterJsonObject(jsonElement.getAsJsonObject());
            Element element = ElementType.valueOf(elementConfig.optString("type")).getElement();
            if (element != null) {
                element.loadJson(new BetterJsonObject(elementConfig.get("settings").getAsJsonObject()));
                manager.addElement(element);
            }
        });
    }

    public void loadOld() {
        EvergreenHUD.LOGGER.info("Converting old configuration system.");

        List<Element> availableElements = new ArrayList<>();
        for (ElementType type : ElementType.values()) {
            availableElements.add(type.getElement());
        }

        for (File config : oldConfigFolder.listFiles()) {
            for (Element element : availableElements) {
                if (config.getName().equalsIgnoreCase(element.getMetadata().getName() + ".json")) {
                    BetterJsonObject root;
                    try {
                        root = BetterJsonObject.getFromFile(config);
                    } catch (Exception e) {
                        save();
                        return;
                    }

                    if (root.optBoolean("enabled")) {
                        element.loadJson(root);

                        // Add image
//                        if (ElementType.getType(element) == ElementType.IMAGE) {
//                            ((StringSetting) element.getCustomSettings().stream()
//                                    .filter(s -> s.getName().equalsIgnoreCase("file path"))
//                                    .findFirst()
//                                    .get()).set(ElementImage.imageFile.getPath());
//                        }

                        manager.addElement(element);
                    }

                    break;
                }
            }
            config.delete();
        }
        oldConfigFolder.delete();
    }

}
