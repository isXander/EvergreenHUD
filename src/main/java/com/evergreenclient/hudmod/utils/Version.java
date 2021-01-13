/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.hudmod.utils;

public class Version {

    private int major, minor, patch;

    public Version(String version) {
        String[] split = version.split("\\.");
        if (split.length > 0) {
            major = Integer.parseInt(split[0]);
        } else {
            minor = patch = 0;
        }
        if (split.length > 1) {
            minor = Integer.parseInt(split[1]);
        } else {
            patch = 0;
        }
        if (split.length > 2) {
            patch = Integer.parseInt(split[2]);
        }
    }

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public static boolean newerThan(Version a, Version b) {
        if (a.getMajor() > b.getMajor())
            return true;
        if (a.getMinor() > b.getMinor())
            return true;
        if (a.getPatch() > b.getPatch())
            return true;
        return false;
    }

    public static boolean olderThan(Version a, Version b) {
        if (a.getMajor() < b.getMajor())
            return true;
        if (a.getMinor() < b.getMinor())
            return true;
        if (a.getPatch() < b.getPatch())
            return true;
        return false;
    }

    public static boolean sameVersion(Version a, Version b) {
        return a.getMajor() == b.getMajor() && a.getMinor() == b.getMinor() && a.getPatch() == b.getPatch();
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getPatch() {
        return patch;
    }

    public void setPatch(int patch) {
        this.patch = patch;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }


}
