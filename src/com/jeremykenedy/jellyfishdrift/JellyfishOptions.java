package com.jeremykenedy.jellyfishdrift;

import java.util.Random;

public final class JellyfishOptions {
  public final String scene;
  public final float speed;

  private JellyfishOptions(String scene, float speed) {
    this.scene = scene;
    this.speed = speed;
  }

  public static JellyfishOptions resolve(
      String background,
      String density,
      String motion,
      String species,
      String lightRays,
      boolean randomizeAll,
      Random random) {
    String environment = choose(background, randomizeAll, random, "dark", "light");
    String playback = choose(motion, randomizeAll, random, "normal", "slow", "fast");
    return new JellyfishOptions(
        "light".equals(environment) ? "aurelia" : "crossota", speedFor(playback));
  }

  private static String choose(
      String selected, boolean randomizeAll, Random random, String... values) {
    if (randomizeAll || "random".equals(selected)) return values[random.nextInt(values.length)];
    for (String value : values) if (value.equals(selected)) return selected;
    return values[0];
  }

  static float speedFor(String motion) {
    if ("slow".equals(motion)) return 0.9f;
    if ("fast".equals(motion)) return 1.25f;
    return 1.0f;
  }
}
