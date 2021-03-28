/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.utils;

import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BetterJsonObject {
    private final Gson pp;
    private JsonObject data;

    public BetterJsonObject() {
        pp = new GsonBuilder().setPrettyPrinting().create();
        data = new JsonObject();
    }

    public BetterJsonObject(String jsonIn) {
        this.pp = new GsonBuilder().setPrettyPrinting().create();
        if (jsonIn == null || jsonIn.isEmpty()) {
            this.data = new JsonObject();
            return;
        }
        try {
            this.data = new JsonParser().parse(jsonIn).getAsJsonObject();
        } catch (JsonSyntaxException | JsonIOException e) {
            e.printStackTrace();
        }
    }

    public BetterJsonObject(JsonObject objectIn) {
        this.pp = new GsonBuilder().setPrettyPrinting().create();
        this.data = ((objectIn != null) ? objectIn : new JsonObject());
    }

    public String optString(String key) {
        return optString(key, "");
    }

    public String optString(String key, String value) {
        if (key == null || key.isEmpty() || !has(key))
            return value;

        JsonPrimitive prim = asPrimitive(get(key));
        if (prim != null && prim.isString())
            return prim.getAsString();

        return value;
    }

    public int optInt(final String key) {
        return optInt(key, 0);
    }

    public int optInt(final String key, final int value) {
        if (key == null || key.isEmpty() || !this.has(key)) {
            return value;
        }
        final JsonPrimitive primitive = this.asPrimitive(this.get(key));
        try {
            if (primitive != null && primitive.isNumber()) {
                return primitive.getAsInt();
            }
        } catch (NumberFormatException ignored) {
        }
        return value;
    }

    public float optFloat(final String key) {
        return this.optFloat(key, 0f);
    }

    public float optFloat(final String key, final float value) {
        if (key == null || key.isEmpty() || !this.has(key))
            return value;
        final JsonPrimitive primitive = this.asPrimitive(this.get(key));
        try {
            if (primitive != null && primitive.isNumber())
                return primitive.getAsFloat();
        } catch (NumberFormatException ignored) {
        }
        return value;
    }

    public double optDouble(final String key) {
        return this.optDouble(key, 0.0);
    }

    public double optDouble(final String key, final double value) {
        if (key == null || key.isEmpty() || !this.has(key)) {
            return value;
        }
        final JsonPrimitive primitive = this.asPrimitive(this.get(key));
        try {
            if (primitive != null && primitive.isNumber()) {
                return primitive.getAsDouble();
            }
        } catch (NumberFormatException ignored) {
        }
        return value;
    }

    public boolean optBoolean(final String key) {
        return this.optBoolean(key, false);
    }

    public boolean optBoolean(final String key, final boolean value) {
        if (key == null || key.isEmpty() || !this.has(key)) {
            return value;
        }
        final JsonPrimitive primitive = this.asPrimitive(this.get(key));
        if (primitive != null && primitive.isBoolean()) {
            return primitive.getAsBoolean();
        }
        return value;
    }

    public boolean has(final String key) {
        return this.data.has(key);
    }

    public JsonElement get(final String key) {
        return this.data.get(key);
    }

    public JsonObject getData() {
        return this.data;
    }

    public BetterJsonObject addProperty(final String key, final String value) {
        if (key != null) {
            this.data.addProperty(key, value);
        }
        return this;
    }

    public BetterJsonObject addProperty(final String key, final Number value) {
        if (key != null) {
            this.data.addProperty(key, value);
        }
        return this;
    }

    public BetterJsonObject addProperty(final String key, final Boolean value) {
        if (key != null) {
            this.data.addProperty(key, value);
        }
        return this;
    }

    public BetterJsonObject add(final String key, final BetterJsonObject object) {
        if (key != null) {
            this.data.add(key, object.data);
        }
        return this;
    }

    public void writeToFile(final File file) {
        if (file == null || (file.exists() && file.isDirectory())) {
            return;
        }
        try {
            if (!file.exists()) {
                final File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }
            final FileWriter writer = new FileWriter(file);
            final BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(this.toPrettyString());
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonPrimitive asPrimitive(final JsonElement element) {
        return (element instanceof JsonPrimitive) ? element.getAsJsonPrimitive() : null;
    }

    public List<String> getAllKeys() {
        List<String> keys = new ArrayList<>();
        Set<Map.Entry<String, JsonElement>> entrySet = data.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    @Override
    public String toString() {
        return this.data.toString();
    }

    public String toPrettyString() {
        return this.pp.toJson(data);
    }

    public static BetterJsonObject getFromFile(File file) throws IOException {
        if (!file.getParentFile().exists() || !file.exists())
            throw new FileNotFoundException();

        BufferedReader f = new BufferedReader(new FileReader(file));
        List<String> lines = f.lines().collect(Collectors.toList());
        f.close();
        if (lines.isEmpty())
            return new BetterJsonObject();
        String builder = String.join("", lines);
        if (builder.trim().length() > 0)
            return new BetterJsonObject(builder.trim());
        return new BetterJsonObject();
    }

    public static BetterJsonObject getFromLines(List<String> lines) {
        if (lines.isEmpty())
            return new BetterJsonObject();
        String builder = String.join("", lines);
        if (builder.trim().length() > 0)
            return new BetterJsonObject(builder.trim());
        return new BetterJsonObject();
    }
}
