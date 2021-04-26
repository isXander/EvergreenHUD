/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/Evergreen-Client/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package co.uk.isxander.evergreenhud.config;

import co.uk.isxander.evergreenhud.elements.Element;
import co.uk.isxander.evergreenhud.elements.ElementManager;
import co.uk.isxander.evergreenhud.elements.ElementType;
import co.uk.isxander.xanderlib.utils.Constants;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import co.uk.isxander.evergreenhud.EvergreenHUD;
import com.google.gson.JsonArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ElementConfig implements Constants {

    public static final int VERSION = 2;
    public static final File OLD_CONFIG_FOLDER = new File(EvergreenHUD.DATA_DIR, "elements");
    public static final File CONFIG_FILE = new File(EvergreenHUD.DATA_DIR, "elements.json");

    private final ElementManager manager;

    public ElementConfig(ElementManager manager) {
        this.manager = manager;
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
        root.writeToFile(CONFIG_FILE);
    }

    public void load() {
        if (!CONFIG_FILE.exists() && OLD_CONFIG_FOLDER.exists()) {
            loadOld();

            save();
            return;
        }

        BetterJsonObject root;
        try {
            root = BetterJsonObject.getFromFile(CONFIG_FILE);
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

        for (File config : OLD_CONFIG_FOLDER.listFiles()) {
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
        OLD_CONFIG_FOLDER.delete();
    }

}
