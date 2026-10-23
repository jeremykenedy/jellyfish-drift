package com.jeremykenedy.jellyfishdrift;

final class FramePacer {
  private static final long FRAME_NANOS = 1_000_000_000L / 30L;
  // Display timestamps can fall just before a deadline because of refresh-rate rounding.
  private static final long TOLERANCE_NANOS = 500_000L;
  private long nextFrameNanos;

  void reset() {
    nextFrameNanos = 0L;
  }

  boolean shouldDraw(long frameTimeNanos) {
    if (nextFrameNanos == 0L) nextFrameNanos = frameTimeNanos;
    long readyAt = frameTimeNanos + TOLERANCE_NANOS;
    if (readyAt < nextFrameNanos) return false;
    long missedPeriods = (readyAt - nextFrameNanos) / FRAME_NANOS;
    nextFrameNanos += (missedPeriods + 1L) * FRAME_NANOS;
    return true;
  }
}
