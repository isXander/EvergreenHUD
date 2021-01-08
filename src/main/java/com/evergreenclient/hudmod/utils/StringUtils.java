/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.utils;

public class StringUtils {

    public static String firstUpper(String original) {
        if (original.length() == 1) return original.toUpperCase();
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

}
