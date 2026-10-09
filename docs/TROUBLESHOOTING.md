# Troubleshooting

## The screensaver is not listed

Confirm the APK is installed, then reopen the device's Display, Ambient mode, or Screensaver selection page. Some vendor firmware does not expose DreamService selection through the same menu.

## The screensaver is listed but does not start

Select Jellyfish Drift in system screensaver settings and check the device's idle timeout. This app does not change selection or timers. Try the in-app animation preview to distinguish a renderer issue from a device activation issue.

## ADB cannot install the package

Check `adb devices`, approve the TV's debugging prompt, and use the exact serial with `--serial`. If installation reports a signature conflict, remove the older test build or build an update with the same signing key as the installed version.

## External settings controls fail

The host must query the `schema` URI, use a key and value returned by the schema, and update the `settings` URI with both `key` and `value`. Unsupported values are rejected.
