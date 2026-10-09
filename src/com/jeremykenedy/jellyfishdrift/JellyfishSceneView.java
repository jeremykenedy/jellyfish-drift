package com.jeremykenedy.jellyfishdrift;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.Random;

final class JellyfishSceneView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private final Random random = new Random(40319L);
    private final Bitmap[] species = new Bitmap[3];
    private final float[] particleX = new float[86];
    private final float[] particleY = new float[86];
    private final float[] particleSize = new float[86];
    private final Matrix rayMatrix = new Matrix();
    private LinearGradient backgroundGradient;
    private LinearGradient rayGradient;
    private LinearGradient shimmerGradient;
    private JellyfishOptions options;
    private Jelly[] jellyfish;
    private long startedAt;
    private boolean running;

    JellyfishSceneView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        species[0] = BitmapFactory.decodeResource(getResources(), R.drawable.jellyfish_moon);
        species[1] = BitmapFactory.decodeResource(getResources(), R.drawable.jellyfish_nettle);
        species[2] = BitmapFactory.decodeResource(getResources(), R.drawable.jellyfish_stripe);
        for (int i = 0; i < particleX.length; i++) {
            particleX[i] = random.nextFloat();
            particleY[i] = random.nextFloat();
            particleSize[i] = 0.7f + random.nextFloat() * 2.5f;
        }
        loadOptions();
    }

    void start() {
        if (!running) {
            running = true;
            startedAt = SystemClock.uptimeMillis();
            postInvalidateOnAnimation();
        }
    }

    void stop() {
        running = false;
        removeCallbacks(invalidator);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getWidth() == 0 || getHeight() == 0) return;
        float time = (SystemClock.uptimeMillis() - startedAt) / 1000f;
        drawBackground(canvas, time);
        drawParticles(canvas, time);
        for (int i = 0; i < jellyfish.length; i++) drawJellyfish(canvas, jellyfish[i], time);
        if (running) postDelayed(invalidator, 33L);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        if (width <= 0 || height <= 0) return;
        int top = options.lightBackground ? 0xffb4d9d8 : 0xff061527;
        int bottom = options.lightBackground ? 0xffe0d8bd : 0xff020711;
        backgroundGradient = new LinearGradient(0, 0, 0, height, top, bottom, Shader.TileMode.CLAMP);
        int alpha = options.lightBackground ? 25 : 37;
        shimmerGradient = new LinearGradient(0, 0, 0, height * 0.72f,
                new int[] {withAlpha(0xfff2f6d4, alpha), withAlpha(0xff8edce6, 0), withAlpha(0xff8edce6, 0)},
                new float[] {0f, 0.42f, 1f}, Shader.TileMode.CLAMP);
        rayGradient = new LinearGradient(0, 0, width * 0.1f, height * 0.76f,
                withAlpha(0xffd7fff1, 17), withAlpha(0xffd7fff1, 0), Shader.TileMode.CLAMP);
    }

    private final Runnable invalidator = new Runnable() {
        @Override
        public void run() {
            if (running) invalidate();
        }
    };

    private void loadOptions() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Random randomChoice = new Random(System.currentTimeMillis());
        options = JellyfishOptions.resolve(
                preferences.getString("background", "dark"),
                preferences.getString("density", "handful"),
                preferences.getString("motion", "normal"),
                preferences.getString("species", "moon"),
                preferences.getString("light_rays", "off"),
                preferences.getBoolean("randomize_all", false), randomChoice);
        jellyfish = new Jelly[options.count];
        int columns = Math.max(1, (int) Math.ceil(Math.sqrt(options.count * 16f / 9f)));
        int rows = (int) Math.ceil(options.count / (float) columns);
        float maximumScale = 0.42f * (float) Math.sqrt(7f / options.count) * 1.2f;
        float margin = Math.min(0.34f, maximumScale * 0.5f + 0.1f);
        for (int i = 0; i < jellyfish.length; i++) {
            float scale = 0.42f * (float) Math.sqrt(7f / options.count)
                    * (0.8f + random.nextFloat() * 0.4f);
            int row = i / columns;
            int column = i % columns;
            float x = (column + 0.5f + (random.nextFloat() - 0.5f) * 0.24f) / columns;
            float rowPosition = rows == 1 ? 0.5f : row / (float) (rows - 1);
            float centerY = margin + rowPosition * (1f - margin * 2f)
                    + (random.nextFloat() - 0.5f) * 0.04f / rows;
            jellyfish[i] = new Jelly(x, centerY - scale * 0.16f, scale,
                    random.nextFloat() * 6.28f, options.species);
        }
    }

    private void drawBackground(Canvas canvas, float time) {
        paint.setShader(backgroundGradient);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setShader(null);
        if (options.shimmer) drawLightRays(canvas, time);
    }

    private void drawLightRays(Canvas canvas, float time) {
        paint.setShader(shimmerGradient);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setShader(null);
        for (int i = 0; i < 7; i++) {
            float x = getWidth() * (i / 7f + 0.08f) + (float) Math.sin(time * 0.11f + i) * getWidth() * 0.025f;
            rayMatrix.setTranslate(x, 0);
            rayGradient.setLocalMatrix(rayMatrix);
            paint.setShader(rayGradient);
            canvas.drawRect(x - getWidth() * 0.035f, 0, x + getWidth() * 0.035f, getHeight() * 0.76f, paint);
        }
        paint.setShader(null);
    }

    private void drawParticles(Canvas canvas, float time) {
        int color = options.lightBackground ? 0xff426771 : 0xffb6e7ed;
        paint.setColor(color);
        for (int i = 0; i < particleX.length; i++) {
            float y = (particleY[i] * getHeight() - time * (4f + particleSize[i] * 3f) * options.speed) % getHeight();
            if (y < 0) y += getHeight();
            paint.setAlpha(30 + (int) (24 * (0.5f + 0.5f * Math.sin(time * 0.7f + i))));
            canvas.drawCircle(particleX[i] * getWidth(), y, particleSize[i], paint);
        }
        paint.setAlpha(255);
    }

    private void drawJellyfish(Canvas canvas, Jelly jelly, float time) {
        Bitmap image = species[jelly.species];
        float h = getHeight() * jelly.scale;
        float w = h * image.getWidth() / image.getHeight();
        float driftX = (float) Math.sin(time * 0.12f * options.speed + jelly.phase) * getWidth() * 0.035f;
        float driftY = (float) Math.sin(time * 0.31f * options.speed + jelly.phase) * getHeight() * 0.026f;
        float pulse = 1f + 0.035f * (float) Math.sin(time * 1.1f * options.speed + jelly.phase);
        float cx = jelly.x * getWidth() + driftX;
        float cy = jelly.y * getHeight() + driftY;
        updateMesh(jelly, image, time);
        canvas.save();
        canvas.translate(cx - w * pulse * 0.5f, cy - h * pulse * 0.34f);
        canvas.scale(w * pulse / image.getWidth(), h * pulse / image.getHeight());
        paint.setAlpha(options.lightBackground ? 242 : 255);
        canvas.drawBitmapMesh(image, Jelly.MESH_COLUMNS, Jelly.MESH_ROWS, jelly.mesh, 0, null, 0, paint);
        paint.setAlpha(255);
        canvas.restore();
    }

    private void updateMesh(Jelly jelly, Bitmap image, float time) {
        int position = 0;
        for (int row = 0; row <= Jelly.MESH_ROWS; row++) {
            float y = row / (float) Jelly.MESH_ROWS;
            float tail = Math.max(0f, (y - 0.33f) / 0.67f);
            for (int column = 0; column <= Jelly.MESH_COLUMNS; column++) {
                float x = column / (float) Jelly.MESH_COLUMNS;
                float breathing = (float) Math.sin(time * 1.05f * options.speed + jelly.phase) * 0.012f;
                float pulse = y < 0.38f ? (x - 0.5f) * breathing : 0f;
                float wave = (float) Math.sin(time * 0.78f * options.speed + jelly.phase + y * 7.3f + x * 1.1f)
                        * tail * 0.028f;
                jelly.mesh[position++] = (x + pulse + wave) * image.getWidth();
                jelly.mesh[position++] = y * image.getHeight();
            }
        }
    }

    private static int withAlpha(int color, int alpha) {
        return (color & 0x00ffffff) | (Math.max(0, Math.min(255, alpha)) << 24);
    }

    private static final class Jelly {
        static final int MESH_COLUMNS = 12;
        static final int MESH_ROWS = 28;
        final float[] mesh = new float[(MESH_COLUMNS + 1) * (MESH_ROWS + 1) * 2];
        final float x;
        final float y;
        final float scale;
        final float phase;
        final int species;

        Jelly(float x, float y, float scale, float phase, int species) {
            this.x = x;
            this.y = y;
            this.scale = scale;
            this.phase = phase;
            this.species = species;
        }
    }
}
