package com.jeremykenedy.jellyfishdrift;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;
import java.util.Date;

final class JellyfishClock extends View {
  private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final java.text.DateFormat format;

  static View wrap(Context context, View scene) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    if (!preferences.getBoolean("show_clock", false)) return scene;
    FrameLayout content = new FrameLayout(context);
    content.addView(scene, new FrameLayout.LayoutParams(-1, -1));
    content.addView(new JellyfishClock(context), new FrameLayout.LayoutParams(-1, -1));
    return content;
  }

  private JellyfishClock(Context context) {
    super(context);
    format = android.text.format.DateFormat.getTimeFormat(context);
    paint.setColor(Color.WHITE);
    paint.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    paint.setShadowLayer(5, 0, 2, Color.BLACK);
    setFocusable(false);
    setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    paint.setTextSize(Math.min(getWidth(), getHeight()) * 0.045f);
    double phase = SystemClock.elapsedRealtime() * (2 * Math.PI / 600000.0);
    float x = getWidth() * (0.06f + 0.015f * (float) Math.sin(phase));
    float y = getHeight() * (0.10f + 0.015f * (float) Math.cos(phase));
    canvas.drawText(format.format(new Date()), x, y, paint);
    postInvalidateDelayed(1000);
  }
}
