package com.jeremykenedy.jellyfishdrift;

import static org.junit.Assert.*;

import org.junit.Test;

public final class JellyfishGeometryTest {
  @Test
  public void bellClosesAroundItsAxisWithFiniteBoundedCoordinates() {
    float[] bell = JellyfishGeometry.surface(0, 80, 28);
    assertEquals(80 * 28 * 6 * 4, bell.length);
    boolean start = false, end = false;
    for (int i = 0; i < bell.length; i += 4) {
      assertTrue(Float.isFinite(bell[i]));
      assertTrue(bell[i] >= 0 && bell[i] <= 1);
      assertTrue(bell[i + 1] >= 0 && bell[i + 1] <= 1);
      assertEquals(0, bell[i + 2], 0);
      start |= bell[i] == 0;
      end |= bell[i] == 1;
    }
    assertTrue(start && end);
  }

  @Test
  public void trailingSurfacesKeepBothEdgesAndUniqueStrandAttachments() {
    for (int kind = 1; kind <= 2; kind++) {
      int strands = kind == 1 ? 48 : 4;
      float[] ribbons = JellyfishGeometry.surface(kind, strands, 44);
      assertEquals(strands * 44 * 6 * 4 * (kind == 2 ? 8 : 1), ribbons.length);
      java.util.Set<Float> angles = new java.util.HashSet<>();
      for (int i = 0; i < ribbons.length; i += 4) {
        assertTrue(ribbons[i] >= -1 && ribbons[i] <= 1);
        assertTrue(ribbons[i + 1] >= 0 && ribbons[i + 1] <= 1);
        assertEquals(kind, ribbons[i + 2], 0);
        angles.add(ribbons[i + 3]);
      }
      assertEquals(strands, angles.size());
    }
  }
}
