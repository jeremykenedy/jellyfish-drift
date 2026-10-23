package com.jeremykenedy.jellyfishdrift;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public final class PreviewActivity extends Activity {
  private JellyfishSceneView scene;

  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);
    getWindow()
        .getDecorView()
        .setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    scene =
        new JellyfishSceneView(
            this,
            () ->
                new android.app.AlertDialog.Builder(this)
                    .setTitle("Playback unavailable")
                    .setMessage("The bundled scene could not be played on this device.")
                    .setPositiveButton("Back to settings", (dialog, which) -> finish())
                    .show());
    setContentView(scene);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (scene != null) scene.start();
  }

  @Override
  protected void onPause() {
    if (scene != null) scene.stop();
    super.onPause();
  }
}
