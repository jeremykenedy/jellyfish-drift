package com.jeremykenedy.jellyfishdrift;

import org.junit.Test;

public final class CoverageTest {
  @Test
  public void unknownPersistedChoicesResolveToSafeExistingDefaults() {
    JellyfishOptions options =
        JellyfishOptions.resolve(
            "deleted", "deleted", "deleted", "deleted", "deleted", false, new java.util.Random(1));
    org.junit.Assert.assertFalse(options.lightBackground);
    org.junit.Assert.assertEquals(3, options.count);
    org.junit.Assert.assertEquals(0.55f, options.speed, 0);
    org.junit.Assert.assertEquals(0, options.species);
    org.junit.Assert.assertFalse(options.shimmer);
  }

  @Test
  public void validatesSettingResolutionAndProviderValues() {
    JellyfishOptionsTest.main(new String[0]);
  }
}
