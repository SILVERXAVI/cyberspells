/*
 * Decompiled with CFR 0.152.
 */
package com.maxwell.cyber_ware_port.client;

public class ClientCyberwareSettings {
    public static int hudX = 10;
    public static int hudY = 10;
    public static int barX = 10;
    public static int barY = 40;
    public static int slideDirection = 0;
    public static int hudColor = -16711681;

    public static void setHudColorFromHex(String hex) {
        if (hex == null || hex.isEmpty()) {
            return;
        }
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        try {
            if (hex.length() == 6) {
                long parsedValue = Long.parseLong(hex, 16);
                hudColor = (int)(0xFFFFFFFFFF000000L | parsedValue);
            } else if (hex.length() == 8) {
                long parsedValue = Long.parseLong(hex, 16);
                hudColor = (int)parsedValue;
            }
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
    }

    public static String getHudColorAsHex() {
        return String.format("%06X", 0xFFFFFF & hudColor);
    }

    public static float[] getColorFloats() {
        float a = (float)(hudColor >> 24 & 0xFF) / 255.0f;
        float r = (float)(hudColor >> 16 & 0xFF) / 255.0f;
        float g = (float)(hudColor >> 8 & 0xFF) / 255.0f;
        float b = (float)(hudColor & 0xFF) / 255.0f;
        return new float[]{r, g, b, a};
    }
}

