package com.jeremykenedy.jellyfishdrift;

import java.util.Random;

public final class JellyfishOptionsTest {
    public static void main(String[] args) {
        verifyDensitySettings();
        verifyMotionAndSpeciesSettings();
        verifyExplicitAndRandomSelections();
        verifySettingsProviderValues();
        System.out.println("Jellyfish settings tests passed.");
    }

    private static void verifyDensitySettings() {
        check(JellyfishOptions.countFor("few") == 3, "few density");
        check(JellyfishOptions.countFor("handful") == 7, "default density");
        check(JellyfishOptions.countFor("unknown") == 7, "safe density fallback");
        check(JellyfishOptions.countFor("many") == 11, "many density");
        check(JellyfishOptions.countFor("ton") == 17, "ton density");
        check(JellyfishOptions.countFor("schools") == 25, "schools density");
    }

    private static void verifyMotionAndSpeciesSettings() {
        check(JellyfishOptions.speedFor("slow") == 0.55f, "slow motion");
        check(JellyfishOptions.speedFor("normal") == 1f, "normal motion");
        check(JellyfishOptions.speedFor("unknown") == 1f, "safe motion fallback");
        check(JellyfishOptions.speedFor("fast") == 1.65f, "fast motion");
        check(JellyfishOptions.speciesFor("moon") == 0, "moon jelly");
        check(JellyfishOptions.speciesFor("sea_nettle") == 1, "sea nettle");
        check(JellyfishOptions.speciesFor("purple_stripe") == 2, "purple stripe");
        check(JellyfishOptions.speciesFor("unknown") == 0, "safe species fallback");
    }

    private static void verifyExplicitAndRandomSelections() {
        JellyfishOptions explicit = JellyfishOptions.resolve("light", "few", "fast", "sea_nettle", "on", false, new Random(1));
        check(explicit.lightBackground && explicit.count == 3 && explicit.speed == 1.65f
                && explicit.species == 1 && explicit.shimmer, "explicit options");
        JellyfishOptions perSetting = JellyfishOptions.resolve("random", "random", "random", "random", "random", false,
                new Random(27));
        check(perSetting.count >= 3 && perSetting.count <= 25, "random count bounds");
        check(perSetting.speed >= 0.55f && perSetting.speed <= 1.65f, "random speed bounds");
        check(perSetting.species >= 0 && perSetting.species <= 2, "random species bounds");
        JellyfishOptions all = JellyfishOptions.resolve("dark", "few", "slow", "moon", "off", true, new Random(5));
        check(all.count >= 3 && all.count <= 25 && all.speed >= 0.55f && all.speed <= 1.65f
                && all.species >= 0 && all.species <= 2, "randomize all options");
        check(!JellyfishOptions.resolve("dark", "few", "slow", "moon", "off", false, new Random(3)).shimmer,
                "explicit shimmer off");
        check(JellyfishOptions.resolve("light", "many", "fast", "purple_stripe", "on", false, new Random(3)).lightBackground,
                "later explicit options resolve");
        for (int index = 0; index < 5; index++) {
            JellyfishOptions option = JellyfishOptions.resolve("dark", "handful", "normal", "moon", "off", false,
                    new FixedRandom(index));
            check(option.count >= 3 && option.count <= 25, "random density option range");
        }
    }

    private static void verifySettingsProviderValues() {
        check(!SettingsValues.isSupported(null, "dark"), "null key rejected");
        check(!SettingsValues.isSupported("background", null), "null value rejected");
        check(SettingsValues.isSupported("background", "light"), "light background accepted");
        check(SettingsValues.isSupported("background", "dark"), "dark background accepted");
        check(SettingsValues.isSupported("background", "random"), "random background accepted");
        check(!SettingsValues.isSupported("background", "blue"), "invalid background rejected");
        check(SettingsValues.isSupported("density", "few"), "few density accepted");
        check(SettingsValues.isSupported("density", "handful"), "handful density accepted");
        check(SettingsValues.isSupported("density", "many"), "many density accepted");
        check(SettingsValues.isSupported("density", "ton"), "ton density accepted");
        check(SettingsValues.isSupported("density", "schools"), "density accepted");
        check(SettingsValues.isSupported("density", "random"), "random density accepted");
        check(!SettingsValues.isSupported("density", "huge"), "invalid density rejected");
        check(SettingsValues.isSupported("motion", "slow"), "slow motion accepted");
        check(SettingsValues.isSupported("motion", "normal"), "normal motion accepted");
        check(SettingsValues.isSupported("motion", "fast"), "motion accepted");
        check(SettingsValues.isSupported("motion", "random"), "random motion accepted");
        check(!SettingsValues.isSupported("motion", "instant"), "invalid motion rejected");
        check(SettingsValues.isSupported("species", "moon"), "moon jelly accepted");
        check(SettingsValues.isSupported("species", "sea_nettle"), "sea nettle accepted");
        check(SettingsValues.isSupported("species", "purple_stripe"), "purple stripe accepted");
        check(SettingsValues.isSupported("species", "random"), "random species accepted");
        check(!SettingsValues.isSupported("species", "shark"), "invalid species rejected");
        check(SettingsValues.isSupported("light_rays", "off"), "rays off accepted");
        check(SettingsValues.isSupported("light_rays", "on"), "rays accepted");
        check(SettingsValues.isSupported("light_rays", "random"), "random rays accepted");
        check(!SettingsValues.isSupported("light_rays", "yes"), "invalid rays rejected");
        check(SettingsValues.isSupported("randomize_all", "true"), "randomize all accepted");
        check(SettingsValues.isSupported("randomize_all", "false"), "randomize toggle accepted");
        check(!SettingsValues.isSupported("unknown", "value"), "unknown key rejected");
    }

    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
    }

    private static final class FixedRandom extends Random {
        private final int value;

        FixedRandom(int value) {
            this.value = value;
        }

        @Override
        public int nextInt(int bound) {
            return Math.min(value, bound - 1);
        }
    }
}
