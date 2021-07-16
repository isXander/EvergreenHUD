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

package dev.isxander.xanderlib.installer;

import com.google.gson.*;
import net.minecraft.launchwrapper.Launch;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XanderLibInstaller {

    // What version of XanderLib does this mod use
    public static final String DESIRED_VERSION = "1.1.0";
    private static final String VERSION_URL = "https://raw.githubusercontent.com/isXander/XanderLib/main/version.json";
    private static File dataDir = null;
    private static boolean isInstalled = false;

    private static boolean isInstalled() {
        return isInstalled;
    }

    public static int initialize(File gameDir) {
        if (isInstalled()) {
            return -1;
        }
        dataDir = new File(gameDir, "xanderlib");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        File data = new File(dataDir, "metadata.json");

        File jar = new File(dataDir, "/" + DESIRED_VERSION + "/xanderlib.jar");
        new File(dataDir, "/" + DESIRED_VERSION).mkdirs();

        boolean metaExists = data.exists();
        JsonHolder metadata = null;
        if (metaExists)
            metadata = readFile(data);
        if (!metaExists || !metadata.has("installed_versions") || !jsonArrayContains(metadata.optJSONArray("installed_versions"), DESIRED_VERSION)) {
            download("https://static.isxander.dev/mods/xanderlib/" + DESIRED_VERSION + ".jar", DESIRED_VERSION, jar, metadata);
        }

        addToClasspath(jar);

        isInstalled = true;
        return 0;
    }

    public static void addToClasspath(File file) {
        try {
            URL url = file.toURI().toURL();

            ClassLoader classLoader = Launch.classLoader.getClass().getClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }


    }

    public static JsonHolder readFile(File in) {
        try {
            return new JsonHolder(FileUtils.readFileToString(in));
        } catch (IOException ignored) {

        }
        return new JsonHolder();
    }

    public static boolean inClasspath() {
        try {
            Class.forName("co.uk.isxander.xanderlib.XanderLib", false, XanderLibInstaller.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean download(String url, String version, File file, JsonHolder versionData) {
        System.out.println("Downloading XanderLib v" + version + " <- " + url);

        url = url.replace(" ", "%20");


        HttpURLConnection connection = null;
        InputStream is = null;
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 (XanderLib Automatic Installer)");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            is = connection.getInputStream();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) > 0) {
                outputStream.write(buffer, 0, read);
            }

            if (versionData == null) versionData = new JsonHolder();
            JsonArray arr = versionData.defaultOptJSONArray("installed_versions", new JsonArray());
            arr.add(new JsonPrimitive(version));
            versionData.put("installed_versions", (JsonElement) arr);

            FileUtils.write(new File(dataDir, "metadata.json"), versionData.toString(), Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.disconnect();
                }

                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                System.out.println("Failed to clean up XanderLib Automatic Installer.");
                e.printStackTrace();
            }
        }
        return true;
    }

    public static JsonHolder fetchJSON(String url) {
        return new JsonHolder(fetchString(url));
    }

    public static String fetchString(String url) {
        url = url.replace(" ", "%20");
        System.out.println("Fetching " + url);

        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 (Sk1er ModCore)");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            is = connection.getInputStream();
            return IOUtils.toString(is, Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.disconnect();
                }

                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                System.out.println("Failed cleaning up ModCoreInstaller#fetchString");
                e.printStackTrace();
            }
        }

        return "Failed to fetch";
    }

    private static boolean jsonArrayContains(JsonArray arr, String value) {
        for (JsonElement element : arr) {
            if (element.isJsonPrimitive()) {
                JsonPrimitive primitive = (JsonPrimitive) element;
                if (primitive.isString()) {
                    if (primitive.getAsString().equals(value)) return true;
                }
            }
        }

        return false;
    }

    public static class JsonHolder {
        private JsonObject object;

        public JsonHolder(JsonObject object) {
            this.object = object;
        }

        public JsonHolder(String raw) {
            if (raw == null)
                object = new JsonObject();
            else
                try {
                    this.object = new JsonParser().parse(raw).getAsJsonObject();
                } catch (Exception e) {
                    this.object = new JsonObject();
                    e.printStackTrace();
                }
        }

        public JsonHolder() {
            this(new JsonObject());
        }

        @Override
        public String toString() {
            if (object != null)
                return object.toString();
            return "{}";
        }

        public JsonHolder put(String key, boolean value) {
            object.addProperty(key, value);
            return this;
        }

        public void mergeNotOverride(JsonHolder merge) {
            merge(merge, false);
        }

        public void mergeOverride(JsonHolder merge) {
            merge(merge, true);
        }

        public void merge(JsonHolder merge, boolean override) {
            JsonObject object = merge.getObject();
            for (String s : merge.getKeys()) {
                if (override || !this.has(s))
                    put(s, object.get(s));
            }
        }

        public void put(String s, JsonElement element) {
            this.object.add(s, element);
        }

        public JsonHolder put(String key, String value) {
            object.addProperty(key, value);
            return this;
        }

        public JsonHolder put(String key, int value) {
            object.addProperty(key, value);
            return this;
        }

        public JsonHolder put(String key, double value) {
            object.addProperty(key, value);
            return this;
        }

        public JsonHolder put(String key, long value) {
            object.addProperty(key, value);
            return this;
        }

        private JsonHolder defaultOptJSONObject(String key, JsonObject fallBack) {
            try {
                return new JsonHolder(object.get(key).getAsJsonObject());
            } catch (Exception e) {
                return new JsonHolder(fallBack);
            }
        }

        public JsonArray defaultOptJSONArray(String key, JsonArray fallback) {
            try {
                return object.get(key).getAsJsonArray();
            } catch (Exception e) {
                return fallback;
            }
        }

        public JsonArray optJSONArray(String key) {
            return defaultOptJSONArray(key, new JsonArray());
        }


        public boolean has(String key) {
            return object.has(key);
        }

        public long optLong(String key, long fallback) {
            try {
                return object.get(key).getAsLong();
            } catch (Exception e) {
                return fallback;
            }
        }

        public long optLong(String key) {
            return optLong(key, 0);
        }

        public boolean optBoolean(String key, boolean fallback) {
            try {
                return object.get(key).getAsBoolean();
            } catch (Exception e) {
                return fallback;
            }
        }

        public boolean optBoolean(String key) {
            return optBoolean(key, false);
        }

        public JsonObject optActualJSONObject(String key) {
            try {
                return object.get(key).getAsJsonObject();
            } catch (Exception e) {
                return new JsonObject();
            }
        }

        public JsonHolder optJSONObject(String key) {
            return defaultOptJSONObject(key, new JsonObject());
        }


        public int optInt(String key, int fallBack) {
            try {
                return object.get(key).getAsInt();
            } catch (Exception e) {
                return fallBack;
            }
        }

        public int optInt(String key) {
            return optInt(key, 0);
        }


        public String defaultOptString(String key, String fallBack) {
            try {
                return object.get(key).getAsString();
            } catch (Exception e) {
                return fallBack;
            }
        }

        public String optString(String key) {
            return defaultOptString(key, "");
        }


        public double optDouble(String key, double fallBack) {
            try {
                return object.get(key).getAsDouble();
            } catch (Exception e) {
                return fallBack;
            }
        }

        public List<String> getKeys() {
            List<String> tmp = new ArrayList<>();
            for (Map.Entry<String, JsonElement> e : object.entrySet()) {
                tmp.add(e.getKey());
            }
            return tmp;
        }

        public double optDouble(String key) {
            return optDouble(key, 0.0);
        }


        public JsonObject getObject() {
            return object;
        }

        public boolean isNull(String key) {
            return object.has(key) && object.get(key).isJsonNull();
        }

        public JsonHolder put(String values, JsonHolder values1) {
            return put(values, values1.getObject());
        }

        public JsonHolder put(String values, JsonObject object) {
            this.object.add(values, object);
            return this;
        }

        public void put(String blacklisted, JsonArray jsonElements) {
            this.object.add(blacklisted, jsonElements);
        }

        public void remove(String header) {
            object.remove(header);
        }
    }

}
