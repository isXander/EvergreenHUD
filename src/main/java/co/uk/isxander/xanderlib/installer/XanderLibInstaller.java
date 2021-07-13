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

package co.uk.isxander.xanderlib.installer;

import club.sk1er.mods.core.ModCoreInstaller;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import club.sk1er.mods.core.ModCoreInstaller.JsonHolder;

public class XanderLibInstaller {

    // What version of XanderLib does this mod use
    public static final String DESIRED_VERSION = "1.0";
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
            metadata = ModCoreInstaller.readFile(data);
        if (!metaExists || !metadata.has("installed_versions") || !jsonArrayContains(metadata.optJSONArray("installed_versions"), DESIRED_VERSION)) {
            download("https://static.isxander.dev/mods/xanderlib/" + DESIRED_VERSION + ".jar", DESIRED_VERSION, jar, metadata);
        }

        ModCoreInstaller.addToClasspath(jar);

        isInstalled = true;
        return 0;
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

            JsonArray arr = versionData.defaultOptJSONArray("installed_versions", new JsonArray());
            JsonPrimitive versionPrim = new JsonPrimitive(version);
            if (!jsonArrayContains(arr, version))
                arr.add(versionPrim);
            versionData.put("version", versionPrim);
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

}
