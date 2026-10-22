package com.jeremykenedy.jellyfishdrift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Parametric surfaces are deformed on the GPU, so tentacles never stretch a photograph. */
final class JellyfishGeometry {
  private JellyfishGeometry() {}

  static final class IndexedSurface {
    final float[] parameters;
    final short[] indices;

    IndexedSurface(float[] parameters, short[] indices) {
      this.parameters = parameters;
      this.indices = indices;
    }
  }

  static IndexedSurface indexed(float[] triangles) {
    if (triangles.length % 4 != 0) {
      throw new IllegalArgumentException("Each vertex needs four surface parameters");
    }
    Map<List<Float>, Integer> positions = new HashMap<>();
    List<Float> parameters = new ArrayList<>();
    short[] indices = new short[triangles.length / 4];
    for (int i = 0; i < triangles.length; i += 4) {
      List<Float> vertex =
          Arrays.asList(triangles[i], triangles[i + 1], triangles[i + 2], triangles[i + 3]);
      Integer index = positions.get(vertex);
      if (index == null) {
        index = positions.size();
        if (index > 65535) {
          throw new IllegalArgumentException("Surface exceeds unsigned-short index capacity");
        }
        positions.put(vertex, index);
        parameters.addAll(vertex);
      }
      indices[i / 4] = index.shortValue();
    }
    float[] result = new float[parameters.size()];
    for (int i = 0; i < result.length; i++) result[i] = parameters.get(i);
    return new IndexedSurface(result, indices);
  }

  static float[] surface(int kind, int strands, int segments) {
    List<Float> vertices = new ArrayList<>();
    for (int strand = 0; strand < strands; strand++) {
      for (int segment = 0; segment < segments; segment++) {
        float a = strand / (float) strands;
        float b = (strand + 1f) / strands;
        float c = segment / (float) segments;
        float d = (segment + 1f) / segments;
        if (kind == 0) {
          quad(vertices, a, b, c, d, kind, 0);
        } else {
          int folds = kind == 2 ? 8 : 1;
          for (int fold = 0; fold < folds; fold++) {
            quad(vertices, -1f + 2f * fold / folds, -1f + 2f * (fold + 1) / folds, c, d, kind, a);
          }
        }
      }
    }
    float[] result = new float[vertices.size()];
    for (int i = 0; i < result.length; i++) result[i] = vertices.get(i);
    return result;
  }

  private static void quad(
      List<Float> output,
      float left,
      float right,
      float top,
      float bottom,
      int kind,
      float strand) {
    vertex(output, left, top, kind, strand);
    vertex(output, right, top, kind, strand);
    vertex(output, left, bottom, kind, strand);
    vertex(output, left, bottom, kind, strand);
    vertex(output, right, top, kind, strand);
    vertex(output, right, bottom, kind, strand);
  }

  private static void vertex(List<Float> output, float x, float y, int kind, float strand) {
    output.add(x);
    output.add(y);
    output.add((float) kind);
    output.add(strand);
  }
}
