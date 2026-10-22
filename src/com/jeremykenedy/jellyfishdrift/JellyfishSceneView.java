package com.jeremykenedy.jellyfishdrift;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import java.util.Random;

final class JellyfishSceneView extends GLSurfaceView {
  private boolean active;

  JellyfishSceneView(Context context) {
    super(context);
    setEGLContextClientVersion(2);
    setEGLConfigChooser(8, 8, 8, 8, 16, 0);
    setPreserveEGLContextOnPause(true);
    SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
    JellyfishOptions options =
        JellyfishOptions.resolve(
            p.getString("background", "dark"),
            p.getString("density", "handful"),
            p.getString("motion", "normal"),
            p.getString("species", "moon"),
            p.getString("light_rays", "off"),
            p.getBoolean("randomize_all", false),
            new Random());
    setRenderer(new JellyfishRenderer(options, context.getAssets(), context.getResources()));
    setRenderMode(RENDERMODE_CONTINUOUSLY);
  }

  void start() {
    if (!active) {
      active = true;
      onResume();
    }
  }

  void stop() {
    if (active) {
      active = false;
      onPause();
    }
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    active = false;
    super.surfaceDestroyed(holder);
  }
}
