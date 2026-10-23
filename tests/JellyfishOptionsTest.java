package com.jeremykenedy.jellyfishdrift;

import java.util.Random;

public final class JellyfishOptionsTest {
  public static void main(String[] args) {
    verifyFilmedOptions();
    verifyRandomSelections();
    verifySettingsProviderValues();
    System.out.println("Jellyfish settings tests passed.");
  }

  private static void verifyFilmedOptions() {
    JellyfishOptions dark =
        JellyfishOptions.resolve(
            "dark", "schools", "normal", "sea_nettle", "on", false, new Random(1));
    check("crossota".equals(dark.scene) && dark.speed == 1f, "dark selects the deep-ocean film");
    JellyfishOptions light =
        JellyfishOptions.resolve("light", "few", "fast", "moon", "off", false, new Random(1));
    check(
        "aurelia".equals(light.scene) && light.speed == 1.25f,
        "light selects the blue-aquarium film");
    check(JellyfishOptions.speedFor("slow") == 0.9f, "slow is 0.9x");
    check(JellyfishOptions.speedFor("normal") == 1f, "normal is recorded speed");
    check(JellyfishOptions.speedFor("unknown") == 1f, "invalid speed falls back to recorded speed");
    check(JellyfishOptions.speedFor("fast") == 1.25f, "fast is 1.25x");
    JellyfishOptions invalid =
        JellyfishOptions.resolve("unknown", "few", "unknown", "moon", "off", false, new Random(1));
    check("crossota".equals(invalid.scene) && invalid.speed == 1f, "invalid options are safe");
    JellyfishOptions legacy =
        JellyfishOptions.resolve(
            "dark",
            "random",
            "normal",
            "random",
            "random",
            false,
            new Random() {
              @Override
              public int nextInt(int bound) {
                throw new AssertionError("inactive values consumed randomness");
              }
            });
    check(
        "crossota".equals(legacy.scene) && legacy.speed == 1f,
        "legacy controls do not affect film");
  }

  private static void verifyRandomSelections() {
    for (int index = 0; index < 3; index++) {
      JellyfishOptions perSetting =
          JellyfishOptions.resolve(
              "random", "few", "random", "moon", "off", false, new FixedRandom(index));
      JellyfishOptions all =
          JellyfishOptions.resolve(
              "light", "schools", "fast", "sea_nettle", "on", true, new FixedRandom(index));
      check(
          perSetting.scene.equals(all.scene) && perSetting.speed == all.speed,
          "only effective settings randomized");
      check(perSetting.scene.equals(index == 0 ? "crossota" : "aurelia"), "random scene selection");
      check(perSetting.speed == new float[] {1f, 0.9f, 1.25f}[index], "random speed selection");
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
