package com.jeremykenedy.jellyfishdrift;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

final class JellyfishRenderer implements GLSurfaceView.Renderer {
  private final JellyfishOptions options;
  private final AssetManager assets;
  private final float[] vp = new float[16];
  private final float[] model = new float[16];
  private final float[] projection = new float[16];
  private final float[] camera = new float[16];
  private final float[][] animals;
  private final FloatBuffer bell = buffer(JellyfishGeometry.surface(0, 80, 28));
  private final FloatBuffer threads = buffer(JellyfishGeometry.surface(1, 48, 44));
  private final FloatBuffer arms = buffer(JellyfishGeometry.surface(2, 4, 96));
  private final FloatBuffer quad =
      buffer(
          new float[] {
            -1, -1, 0, 0, 1, -1, 0, 0, -1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, 1, 1, 0, 0
          });
  private int animalProgram;
  private int waterProgram;
  private float aspect = 16f / 9f;
  private long lastFrame;
  private float elapsed;

  JellyfishRenderer(JellyfishOptions options, AssetManager assets) {
    this.options = options;
    this.assets = assets;
    animals = new float[options.count][5];
    Random random = new Random(40319);
    for (int i = 0; i < animals.length; i++) {
      float[] a = animals[i];
      float angle = i * 2.3999632f + 0.6f;
      float radius = (float) Math.sqrt((i + 0.4f) / animals.length);
      a[0] = (float) Math.cos(angle) * radius * 3.1f;
      a[1] = (float) Math.sin(angle) * radius * 1.42f + 0.26f;
      a[2] = -0.65f + random.nextFloat() * 0.8f;
      a[3] = (0.70f + random.nextFloat() * 0.45f) * (float) Math.sqrt(7f / options.count);
      a[4] = random.nextFloat() * 6.28f;
    }
    // One nearer animal gives the shoal depth rather than arranging identical rows.
    animals[0][0] = -0.15f;
    animals[0][1] = 0.35f;
    animals[0][2] = 0.4f;
    animals[0][3] *= 1.3f;
    java.util.Arrays.sort(
        animals,
        new java.util.Comparator<float[]>() {
          @Override
          public int compare(float[] a, float[] b) {
            return Float.compare(a[2], b[2]);
          }
        });
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    animalProgram = program("jellyfish.vert", "jellyfish.frag");
    waterProgram = program("water.vert", "water.frag");
    GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    Matrix.setLookAtM(camera, 0, 0, 0, 5.2f, 0, 0, 0, 0, 1, 0);
    lastFrame = 0;
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    GLES20.glViewport(0, 0, width, height);
    aspect = width / (float) height;
    Matrix.orthoM(projection, 0, -2f * aspect, 2f * aspect, -2, 2, 1, 12);
    Matrix.multiplyMM(vp, 0, projection, 0, camera, 0);
  }

  @Override
  public void onDrawFrame(GL10 gl) {
    long now = SystemClock.uptimeMillis();
    if (lastFrame != 0) elapsed += Math.min(0.1f, (now - lastFrame) / 1000f) * options.speed;
    lastFrame = now;
    GLES20.glDisable(GLES20.GL_BLEND);
    GLES20.glUseProgram(waterProgram);
    uniform(waterProgram, "uTime", elapsed);
    uniform(waterProgram, "uLight", options.lightBackground ? 1 : 0);
    uniform(waterProgram, "uRays", options.shimmer ? 1 : 0);
    uniform(waterProgram, "uAspect", aspect);
    draw(waterProgram, quad);
    GLES20.glEnable(GLES20.GL_BLEND);
    GLES20.glUseProgram(animalProgram);
    GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(animalProgram, "uVP"), 1, false, vp, 0);
    uniform(animalProgram, "uTime", elapsed);
    uniform(animalProgram, "uLight", options.lightBackground ? 1 : 0);
    uniform(animalProgram, "uSpecies", options.species);
    for (float[] a : animals) {
      float t = elapsed + a[4];
      float y = a[1] + (float) Math.sin(t * 0.25) * 0.12f + (float) Math.sin(t * 1.45) * 0.025f;
      Matrix.setIdentityM(model, 0);
      Matrix.translateM(model, 0, a[0] + (float) Math.sin(t * 0.13) * 0.20f, y, a[2]);
      Matrix.rotateM(model, 0, 17 + (float) Math.sin(t * 0.18) * 10, 1, 0, 0);
      Matrix.rotateM(model, 0, (float) Math.sin(t * 0.15) * 14, 0, 0, 1);
      Matrix.rotateM(model, 0, a[4] * 57.2958f + elapsed * 1.3f, 0, 1, 0);
      Matrix.scaleM(model, 0, a[3], a[3], a[3]);
      GLES20.glUniformMatrix4fv(
          GLES20.glGetUniformLocation(animalProgram, "uModel"), 1, false, model, 0);
      uniform(animalProgram, "uPhase", a[4]);
      uniform(animalProgram, "uDepth", 0.72f + (a[2] + 0.7f) * 0.30f);
      draw(animalProgram, threads);
      draw(animalProgram, arms);
      GLES20.glEnable(GLES20.GL_CULL_FACE);
      GLES20.glCullFace(GLES20.GL_FRONT);
      draw(animalProgram, bell);
      GLES20.glCullFace(GLES20.GL_BACK);
      draw(animalProgram, bell);
      GLES20.glDisable(GLES20.GL_CULL_FACE);
    }
    long spent = SystemClock.uptimeMillis() - now;
    if (spent < 32) SystemClock.sleep(32 - spent);
  }

  private static void uniform(int program, String name, float value) {
    GLES20.glUniform1f(GLES20.glGetUniformLocation(program, name), value);
  }

  private static void draw(int program, FloatBuffer data) {
    int position = GLES20.glGetAttribLocation(program, "aParam");
    data.position(0);
    GLES20.glEnableVertexAttribArray(position);
    GLES20.glVertexAttribPointer(position, 4, GLES20.GL_FLOAT, false, 16, data);
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, data.capacity() / 4);
    GLES20.glDisableVertexAttribArray(position);
  }

  private int program(String vertex, String fragment) {
    int p = GLES20.glCreateProgram();
    GLES20.glAttachShader(p, shader(GLES20.GL_VERTEX_SHADER, read(vertex)));
    GLES20.glAttachShader(p, shader(GLES20.GL_FRAGMENT_SHADER, read(fragment)));
    GLES20.glLinkProgram(p);
    int[] status = new int[1];
    GLES20.glGetProgramiv(p, GLES20.GL_LINK_STATUS, status, 0);
    if (status[0] == 0) throw new IllegalStateException(GLES20.glGetProgramInfoLog(p));
    return p;
  }

  private static int shader(int kind, String source) {
    int shader = GLES20.glCreateShader(kind);
    GLES20.glShaderSource(shader, source);
    GLES20.glCompileShader(shader);
    int[] status = new int[1];
    GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);
    if (status[0] == 0) throw new IllegalStateException(GLES20.glGetShaderInfoLog(shader));
    return shader;
  }

  private String read(String name) {
    try (InputStream input = assets.open(name);
        ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      byte[] bytes = new byte[4096];
      int count;
      while ((count = input.read(bytes)) != -1) output.write(bytes, 0, count);
      return output.toString("UTF-8");
    } catch (IOException e) {
      throw new IllegalStateException("Missing renderer asset: " + name, e);
    }
  }

  private static FloatBuffer buffer(float[] data) {
    FloatBuffer result =
        ByteBuffer.allocateDirect(data.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    result.put(data).position(0);
    return result;
  }
}
