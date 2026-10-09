package com.jeremykenedy.jellyfishdrift;

public final class SettingsValues {
    private SettingsValues() {}

    public static boolean isSupported(String key, String value) {
        if (key == null || value == null) return false;
        if ("background".equals(key)) return oneOf(value, "dark", "light", "random");
        if ("density".equals(key)) return oneOf(value, "few", "handful", "many", "ton", "schools", "random");
        if ("motion".equals(key)) return oneOf(value, "slow", "normal", "fast", "random");
        if ("species".equals(key)) return oneOf(value, "moon", "sea_nettle", "purple_stripe", "random");
        if ("light_rays".equals(key)) return oneOf(value, "off", "on", "random");
        if ("randomize_all".equals(key)) return oneOf(value, "true", "false");
        return false;
    }

    private static boolean oneOf(String value, String... allowed) {
        for (String option : allowed) if (option.equals(value)) return true;
        return false;
    }
}
