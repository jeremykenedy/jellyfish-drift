package com.jeremykenedy.jellyfishdrift;

public final class FramePacerTest {
  public static void main(String[] args) {
    verifyRefreshRate(60.0);
    verifyRefreshRate(59.94);
    verifyRefreshRate(120.0);
    verifyDelayedCallbacks();
    verifyRestart();
    System.out.println("Jellyfish frame pacing tests passed.");
  }

  private static void verifyRefreshRate(double refreshRate) {
    FramePacer pacer = new FramePacer();
    long origin = 9_000_000_000L;
    int drawn = 0;
    long previousDraw = 0;
    int callbacks = (int) Math.floor(refreshRate * 10);
    for (int frame = 0; frame < callbacks; frame++) {
      long timestamp = origin + Math.round(frame * 1_000_000_000.0 / refreshRate);
      if (pacer.shouldDraw(timestamp)) {
        if (previousDraw != 0) {
          check(timestamp - previousDraw >= 24_000_000L, "no consecutive 60 Hz frames");
        }
        previousDraw = timestamp;
        drawn++;
      }
    }
    check(drawn >= 299 && drawn <= 301, "approximately 30 drawings per second at " + refreshRate);
  }

  private static void verifyDelayedCallbacks() {
    FramePacer pacer = new FramePacer();
    long origin = 1_000_000_000L;
    check(pacer.shouldDraw(origin), "first frame draws");
    check(!pacer.shouldDraw(origin + 16_666_667L), "intermediate vsync is skipped");
    check(pacer.shouldDraw(origin + 40_000_000L), "late frame draws without a new delay");
    check(
        !pacer.shouldDraw(origin + 50_000_000L),
        "late frame does not cause an immediate extra draw");
    check(pacer.shouldDraw(origin + 66_666_667L), "original cadence survives draw delay");
    check(pacer.shouldDraw(origin + 10_000_000_000L), "long pause skips expired deadlines");
    check(!pacer.shouldDraw(origin + 10_000_000_000L), "no catch-up burst at the same timestamp");
    check(!pacer.shouldDraw(origin + 10_016_666_667L), "next vsync remains capped after a pause");
    check(pacer.shouldDraw(origin + 10_033_333_333L), "normal cadence resumes after a pause");
  }

  private static void verifyRestart() {
    FramePacer pacer = new FramePacer();
    check(pacer.shouldDraw(0L), "zero-based first callback works");
    check(!pacer.shouldDraw(1L), "repeated early callback is skipped");
    pacer.reset();
    check(pacer.shouldDraw(1L), "restart draws without waiting for the previous deadline");
  }

  private static void check(boolean condition, String message) {
    if (!condition) throw new AssertionError(message);
  }
}
