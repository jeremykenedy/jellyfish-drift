package com.jeremykenedy.jellyfishdrift;

import java.util.Random;

public final class JellyfishOptions {
    public final boolean lightBackground;
    public final int count;
    public final float speed;
    public final int species;
    public final boolean shimmer;

    private JellyfishOptions(boolean lightBackground, int count, float speed, int species, boolean shimmer) {
        this.lightBackground = lightBackground;
        this.count = count;
        this.speed = speed;
        this.species = species;
        this.shimmer = shimmer;
    }

    public static JellyfishOptions resolve(String background, String density, String motion,
            String species, String lightRays, boolean randomizeAll, Random random) {
        String selectedBackground = choose(background, randomizeAll, random, "dark", "light");
        String selectedDensity = choose(density, randomizeAll, random, "few", "handful", "many", "ton", "schools");
        String selectedMotion = choose(motion, randomizeAll, random, "slow", "normal", "fast");
        String selectedSpecies = choose(species, randomizeAll, random, "moon", "sea_nettle", "purple_stripe");
        String selectedRays = choose(lightRays, randomizeAll, random, "off", "on");
        return new JellyfishOptions("light".equals(selectedBackground), countFor(selectedDensity),
                speedFor(selectedMotion), speciesFor(selectedSpecies), "on".equals(selectedRays));
    }

    private static String choose(String selected, boolean randomizeAll, Random random, String... values) {
        if (randomizeAll || "random".equals(selected)) {
            return values[random.nextInt(values.length)];
        }
        for (String value : values) {
            if (value.equals(selected)) return selected;
        }
        return values[0];
    }

    static int countFor(String density) {
        if ("few".equals(density)) return 3;
        if ("many".equals(density)) return 11;
        if ("ton".equals(density)) return 17;
        if ("schools".equals(density)) return 25;
        return 7;
    }

    static float speedFor(String motion) {
        if ("slow".equals(motion)) return 0.55f;
        if ("fast".equals(motion)) return 1.65f;
        return 1.0f;
    }

    static int speciesFor(String species) {
        if ("sea_nettle".equals(species)) return 1;
        if ("purple_stripe".equals(species)) return 2;
        return 0;
    }
}
