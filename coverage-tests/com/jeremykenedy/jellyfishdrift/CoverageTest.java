package com.jeremykenedy.jellyfishdrift;

import org.junit.Test;

public final class CoverageTest {
  @Test
  public void validatesSettingResolutionAndProviderValues() {
    JellyfishOptionsTest.main(new String[0]);
  }

  @Test
  public void pacesDrawingWithoutAccumulatingDrawTime() {
    FramePacerTest.main(new String[0]);
  }
}
