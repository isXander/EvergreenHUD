/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.utils;

import com.evergreenclient.hudmod.EvergreenHUD;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpsUtils {

    public static void downloadFile(URL url, File destination) throws Exception {
        if (!destination.exists()) {
            URLConnection con = url.openConnection();
            DataInputStream dis = new DataInputStream(con.getInputStream());
            byte[] data = new byte[con.getContentLength()];

            for (int i = 0; i < data.length; i++) {
                data[i] = dis.readByte();
            }

            dis.close();
            FileOutputStream fos = new FileOutputStream(destination);
            fos.write(data);
            fos.close();
        }
    }

    public static String getString(String url) {
        try {
            HttpURLConnection httpClient =
                    (HttpURLConnection) new URL(url).openConnection();
            httpClient.setRequestMethod("GET");
            httpClient.setUseCaches(false);
            httpClient.setRequestProperty("User-Agent", "Evergreen/" + EvergreenHUD.VERSION);
            httpClient.setReadTimeout(15000);
            httpClient.setConnectTimeout(15000);
            httpClient.setDoOutput(true);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()))) {

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                return response.toString();

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
