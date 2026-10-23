package com.jeremykenedy.jellyfishdrift;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import java.io.IOException;
import java.util.Random;

final class JellyfishSceneView extends FrameLayout implements SurfaceHolder.Callback {
  private final SurfaceView surface;
  private final Runnable playbackFailed;
  private MediaPlayer player;
  private boolean resumed;
  private boolean fallback;
  private int position;
  private final JellyfishOptions options;
  private boolean prepared;
  private final android.os.Handler handler =
      new android.os.Handler(android.os.Looper.getMainLooper());
  private Runnable preparationTimeout;

  JellyfishSceneView(Context context, Runnable playbackFailed) {
    super(context);
    this.playbackFailed = playbackFailed;
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    options =
        JellyfishOptions.resolve(
            prefs.getString("background", "dark"),
            prefs.getString("density", "handful"),
            prefs.getString("motion", "normal"),
            prefs.getString("species", "moon"),
            prefs.getString("light_rays", "off"),
            prefs.getBoolean("randomize_all", false),
            new Random());
    setBackgroundColor(Color.BLACK);
    surface = new SurfaceView(context);
    addView(surface, new FrameLayout.LayoutParams(-1, -1, Gravity.CENTER));
    surface.getHolder().addCallback(this);
    addOnLayoutChangeListener((v, l, t, r, b, ol, ot, or, ob) -> fit(r - l, b - t));
  }

  void start() {
    resumed = true;
    preparePlayer();
  }

  void stop() {
    resumed = false;
    release();
  }

  private void fit(int width, int height) {
    if (width <= 0 || height <= 0) return;
    int fittedWidth = Math.min(width, height * 16 / 9);
    int fittedHeight = fittedWidth * 9 / 16;
    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) surface.getLayoutParams();
    if (params.width != fittedWidth || params.height != fittedHeight) {
      params.width = fittedWidth;
      params.height = fittedHeight;
      params.gravity = Gravity.CENTER;
      surface.setLayoutParams(params);
    }
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    preparePlayer();
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    release();
  }

  private void preparePlayer() {
    if (!resumed || player != null || !surface.getHolder().getSurface().isValid()) return;
    MediaPlayer next = new MediaPlayer();
    player = next;
    next.setDisplay(surface.getHolder());
    next.setVolume(0, 0);
    next.setLooping(true);
    next.setOnPreparedListener(
        ready -> {
          if (player != ready || !resumed) return;
          cancelPreparationTimeout();
          prepared = true;
          if (position > 0) ready.seekTo(position);
          try {
            ready.setPlaybackParams(ready.getPlaybackParams().setSpeed(options.speed));
            ready.start();
          } catch (IllegalStateException | IllegalArgumentException error) {
            failPlayer(ready);
            return;
          }
          Log.i(
              "JellyfishFilm",
              "Started "
                  + (fallback ? "720p" : "1080p")
                  + " durationMs="
                  + ready.getDuration()
                  + " metrics="
                  + (android.os.Build.VERSION.SDK_INT >= 26 ? ready.getMetrics() : "unavailable"));
        });
    next.setOnErrorListener(
        (failed, what, extra) -> {
          if (player != failed) return true;
          Log.e("JellyfishFilm", "Decoder error " + what + "/" + extra);
          failPlayer(failed);
          return true;
        });
    try (AssetFileDescriptor file =
        getContext().getAssets().openFd(options.scene + (fallback ? "-720.mp4" : "-1080.mp4"))) {
      next.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
      preparationTimeout =
          () -> {
            if (player == next && !prepared) failPlayer(next);
          };
      handler.postDelayed(preparationTimeout, 12000);
      next.prepareAsync();
    } catch (IOException | IllegalArgumentException | IllegalStateException error) {
      Log.e("JellyfishFilm", "Bundled scene unavailable", error);
      failPlayer(next);
    }
  }

  private void failPlayer(MediaPlayer failed) {
    if (player != failed) return;
    release();
    if (!resumed) return;
    if (!fallback) {
      fallback = true;
      position = 0;
      preparePlayer();
    } else {
      resumed = false;
      playbackFailed.run();
    }
  }

  private void cancelPreparationTimeout() {
    if (preparationTimeout == null) return;
    handler.removeCallbacks(preparationTimeout);
    preparationTimeout = null;
  }

  private void release() {
    cancelPreparationTimeout();
    MediaPlayer current = player;
    player = null;
    if (current == null) return;
    try {
      if (prepared) position = current.getCurrentPosition();
      Log.i(
          "JellyfishFilm",
          "Released positionMs="
              + position
              + " metrics="
              + (android.os.Build.VERSION.SDK_INT >= 26 ? current.getMetrics() : "unavailable"));
    } catch (IllegalStateException ignored) {
      // Surface loss may happen before asynchronous preparation completes.
    }
    prepared = false;
    current.setOnPreparedListener(null);
    current.setOnErrorListener(null);
    current.release();
  }
}
