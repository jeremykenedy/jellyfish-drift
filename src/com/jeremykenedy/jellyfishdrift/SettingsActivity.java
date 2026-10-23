package com.jeremykenedy.jellyfishdrift;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public final class SettingsActivity extends Activity {
  private SharedPreferences preferences;

  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);
    preferences = PreferenceManager.getDefaultSharedPreferences(this);
    ScrollView scroll = new ScrollView(this);
    LinearLayout content = new LinearLayout(this);
    content.setOrientation(LinearLayout.VERTICAL);
    content.setPadding(56, 32, 56, 32);
    scroll.addView(content);
    TextView title = new TextView(this);
    title.setText("Jellyfish Drift settings");
    title.setTextSize(28);
    title.setPadding(0, 0, 0, 20);
    content.addView(title);
    Button preview = new Button(this);
    preview.setText("Preview animation");
    preview.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivity(new Intent(SettingsActivity.this, PreviewActivity.class));
          }
        });
    content.addView(preview);
    addChoice(
        content,
        "Background",
        "background",
        new String[] {"dark", "light", "random"},
        new String[] {"Deep water", "Bright water", "Random"},
        "dark");
    addChoice(
        content,
        "Jellyfish amount",
        "density",
        new String[] {"few", "handful", "many", "ton", "schools", "random"},
        new String[] {"A few", "A handful", "Many", "A ton", "Schools", "Random"},
        "handful");
    addChoice(
        content,
        "Movement",
        "motion",
        new String[] {"slow", "normal", "fast", "random"},
        new String[] {"Slow", "Natural", "Fast", "Random"},
        "normal");
    addChoice(
        content,
        "Jellyfish species",
        "species",
        new String[] {"moon", "sea_nettle", "purple_stripe", "random"},
        new String[] {"Moon jelly", "Pacific sea nettle", "Purple-striped jelly", "Random"},
        "moon");
    addChoice(
        content,
        "Sunlight from above",
        "light_rays",
        new String[] {"off", "on", "random"},
        new String[] {"Off", "On", "Random"},
        "off");
    CheckBox clock = new CheckBox(this);
    clock.setText("Show clock");
    clock.setTextSize(18);
    clock.setChecked(preferences.getBoolean("show_clock", false));
    clock.setOnCheckedChangeListener(
        (button, checked) -> preferences.edit().putBoolean("show_clock", checked).apply());
    content.addView(clock);
    CheckBox randomize = new CheckBox(this);
    randomize.setText("Randomize all settings each time the screensaver starts");
    randomize.setTextSize(18);
    randomize.setChecked(preferences.getBoolean("randomize_all", false));
    randomize.setOnCheckedChangeListener(
        new android.widget.CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(android.widget.CompoundButton button, boolean checked) {
            preferences.edit().putBoolean("randomize_all", checked).apply();
          }
        });
    content.addView(randomize);
    Button credits = new Button(this);
    credits.setText("Credits and licenses");
    credits.setOnClickListener(view -> showCredits());
    content.addView(credits);
    setContentView(scroll);
    preview.setFocusableInTouchMode(true);
    preview.requestFocus();
  }

  private void addChoice(
      LinearLayout content,
      String title,
      String key,
      String[] values,
      String[] labels,
      String fallback) {
    TextView label = new TextView(this);
    label.setText(title);
    label.setTextSize(18);
    label.setPadding(0, 16, 0, 4);
    content.addView(label);
    Spinner spinner = new Spinner(this);
    spinner.setFocusable(true);
    spinner.setFocusableInTouchMode(true);
    spinner.setGravity(Gravity.CENTER_VERTICAL);
    ArrayAdapter<String> adapter =
        new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labels);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    String selected = preferences.getString(key, fallback);
    int selection = 0;
    for (int i = 0; i < values.length; i++) if (values[i].equals(selected)) selection = i;
    spinner.setSelection(selection);
    spinner.setOnItemSelectedListener(
        new android.widget.AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(
              android.widget.AdapterView<?> parent, View view, int position, long id) {
            preferences.edit().putString(key, values[position]).apply();
          }

          @Override
          public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    content.addView(spinner);
  }

  private String asset(String name) throws java.io.IOException {
    try (java.io.InputStream stream = getAssets().open(name)) {
      java.io.ByteArrayOutputStream bytes = new java.io.ByteArrayOutputStream();
      byte[] buffer = new byte[8192];
      int length;
      while ((length = stream.read(buffer)) != -1) bytes.write(buffer, 0, length);
      return new String(bytes.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
    }
  }

  private void showCredits() {
    TextView text = new TextView(this);
    text.setTextSize(20);
    text.setPadding(32, 20, 32, 20);
    try {
      text.setText(asset("NOTICE.txt") + "\n\nApplication code license\n" + asset("LICENSE.txt"));
    } catch (java.io.IOException error) {
      text.setText("Credits could not be loaded. Please reopen settings.");
    }
    ScrollView scroll = new ScrollView(this);
    scroll.setFocusable(true);
    scroll.addView(text);
    new android.app.AlertDialog.Builder(this)
        .setTitle("Credits and licenses")
        .setView(scroll)
        .setPositiveButton("Close", null)
        .show();
  }
}
