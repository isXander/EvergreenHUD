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

package co.uk.isxander.evergreenhud.gui.screens.impl;

import co.uk.isxander.evergreenhud.addon.AddonManager;
import co.uk.isxander.evergreenhud.addon.repo.AddonEntry;
import co.uk.isxander.evergreenhud.gui.components.CategoryScrollPane;
import co.uk.isxander.evergreenhud.gui.screens.GuiScreenExt;
import co.uk.isxander.xanderlib.utils.HttpsUtils;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public class GuiDiscoverAddons extends GuiScreenExt {

    private CategoryScrollPane categoryScrollPane;
    private String currentCategory;
    private BetterJsonObject addonsJson;
    private List<AddonEntry> addons = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

    public GuiDiscoverAddons(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initGui() {
        if (addonsJson == null) {
            addonsJson = new BetterJsonObject(HttpsUtils.getString(AddonManager.REPO_URL + "addons.json"));

            for (String key : addonsJson.getAllKeys()) {
                categories.add(key);

                JsonArray categoryArr = addonsJson.get(key).getAsJsonArray();
                for (JsonElement addonElement : categoryArr) {
                    addons.add(new AddonEntry(new BetterJsonObject(addonElement.getAsJsonObject())));
                }
            }
        }

        this.categoryScrollPane = new CategoryScrollPane(width / 6, height, 0, height, 0, 20, width, height, categories, (category, id) -> {
            currentCategory = category;
            addButtons();
        });
    }

    private void addButtons() {

    }

//    private static class AddonList extends GuiListExtended {
//
//        private static class AddonListEntry implements IGuiListEntry {
//            private final AddonEntry addon;
//
//            @Override
//            public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
//
//            }
//
//            @Override
//            public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
//
//            }
//
//            @Override
//            public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
//                return false;
//            }
//
//            @Override
//            public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
//
//            }
//        }
//
//    }
}
