package co.uk.isxander.xanderlib.installer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XanderLibInstaller {

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
        File jar = new File(dataDir, "xanderlib.jar");
        System.out.println("XanderLib Jar: " + jar.getPath());
        File data = new File(dataDir, "metadata.json");
        System.out.println("Metadata Json: " + data.getPath());
        JsonHolder metadata = readFile(data);
        JsonHolder latestVersion = fetchJSON(VERSION_URL);
        String latest = latestVersion.optString("version");

        if (!jar.exists() || !metadata.optString("version").equalsIgnoreCase(latest)) {
            if (jar.exists()) {
                jar.delete();
            }

            download("https://static.isxander.co.uk/mods/xanderlib/" + latest + ".jar", latest, jar, metadata);
        }

        addToClasspath(jar);

        isInstalled = true;
        System.out.println("XanderLib is now installed.");
        return 0;
    }


    public static void addToClasspath(File file) {
        try {
            URL url = file.toURI().toURL();

            ClassLoader classLoader = XanderLibInstaller.class.getClassLoader();
            Method method = classLoader.getClass().getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    private static boolean download(String url, String version, File file, JsonHolder versionData) {
        url = url.replace(" ", "%20");
        System.out.println("Installing XanderLib: v" + version + " -> " + url);

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
            FileUtils.write(new File(dataDir, "metadata.json"), versionData.put("version", version).toString(), Charset.defaultCharset());
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

    private static JsonHolder readFile(File in) {
        try {
            return new JsonHolder(FileUtils.readFileToString(in, Charset.defaultCharset()));
        } catch (IOException ignored) {

        }
        return new JsonHolder();
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
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 (XanderLib Automatic Installer)");
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
                System.out.println("Failed to clean up XanderLib Automatic Installer.");
                e.printStackTrace();
            }
        }

        return "Failed to fetch";
    }

    static class JsonHolder {
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

        private void put(String s, JsonElement element) {
            this.object.add(s, element);
        }

        public JsonHolder put(String key, String value) {
            object.addProperty(key, value);
            return this;
        }

        public boolean has(String key) {
            return object.has(key);
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

        public List<String> getKeys() {
            List<String> tmp = new ArrayList<>();
            for (Map.Entry<String, JsonElement> e : object.entrySet()) {
                tmp.add(e.getKey());
            }
            return tmp;
        }

        public JsonObject getObject() {
            return object;
        }

    }


}