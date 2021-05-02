package co.uk.isxander.evergreenhud.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class JsonUtils {

    public static boolean jsonArrayContains(JsonArray arr, String val) {
        for (JsonElement entry : arr) {
            String str = entry.getAsString();
            if (str != null && str.equals(val))
                return true;
        }
        return false;
    }

}
