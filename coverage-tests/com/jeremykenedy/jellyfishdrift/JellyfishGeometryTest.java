package com.jeremykenedy.jellyfishdrift;

import static org.junit.Assert.*;

import org.junit.Test;

public final class JellyfishGeometryTest {
  @Test
  public void indexedSurfacesPreserveEveryTriangleAndParameterBit() {
    int[][] surfaces = {{0, 80, 28}, {1, 64, 64}, {1, 24, 64}, {1, 16, 64}, {2, 4, 96}};
    for (int[] surface : surfaces) {
      float[] original = JellyfishGeometry.surface(surface[0], surface[1], surface[2]);
      JellyfishGeometry.IndexedSurface indexed = JellyfishGeometry.indexed(original);
      assertEquals(original.length / 4, indexed.indices.length);
      assertTrue(indexed.parameters.length < original.length / 2);
      for (int vertex = 0; vertex < indexed.indices.length; vertex++) {
        int offset = Short.toUnsignedInt(indexed.indices[vertex]) * 4;
        for (int component = 0; component < 4; component++) {
          assertEquals(
              Float.floatToRawIntBits(original[vertex * 4 + component]),
              Float.floatToRawIntBits(indexed.parameters[offset + component]));
        }
      }
    }
  }

  @Test
  public void indexingHandlesEmptySurfacesAndPreservesSignedZero() {
    JellyfishGeometry.IndexedSurface empty = JellyfishGeometry.indexed(new float[0]);
    assertEquals(0, empty.parameters.length);
    assertEquals(0, empty.indices.length);
    JellyfishGeometry.IndexedSurface zeros =
        JellyfishGeometry.indexed(new float[] {0f, 0f, 0f, 0f, -0f, 0f, 0f, 0f});
    assertEquals(8, zeros.parameters.length);
    assertEquals(0, Short.toUnsignedInt(zeros.indices[0]));
    assertEquals(1, Short.toUnsignedInt(zeros.indices[1]));
  }

  @Test
  public void unsignedShortIndicesRetainTheFullSupportedRange() {
    float[] vertices = uniqueVertices(65536);
    JellyfishGeometry.IndexedSurface indexed = JellyfishGeometry.indexed(vertices);
    assertEquals(65535, Short.toUnsignedInt(indexed.indices[65535]));
    assertArrayEquals(vertices, indexed.parameters, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void indexingRejectsAnIncompleteVertex() {
    JellyfishGeometry.indexed(new float[3]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void indexingRejectsAnUnsupportedVertexCount() {
    JellyfishGeometry.indexed(uniqueVertices(65537));
  }

  private static float[] uniqueVertices(int count) {
    float[] result = new float[count * 4];
    for (int i = 0; i < count; i++) result[i * 4] = i;
    return result;
  }

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
