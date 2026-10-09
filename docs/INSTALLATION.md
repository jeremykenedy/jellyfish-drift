# Installation, update, and removal

The standalone installer downloads the latest stable release APK and its `.sha256` file from this repository, verifies the checksum, and then installs or updates Jellyfish Drift. It accepts only this repository's release asset URLs and GitHub's release asset hosts. The Android app itself has no network permission or network requests. The installer sends no device or usage data.

Connect the TV to the same network as this computer, enable ADB debugging on the TV, then run:

```bash
python3 install.py --serial TV_IP:5555
```

The installer supports Fire TV, Android TV, and Google TV devices that expose ADB. If one authorized TV is connected, `--serial` can be omitted. If several are connected, specify one. Use `--yes` for unattended installation; when multiple TVs are connected, `--serial` is still required.

For a local build, run `bash build.sh`, then install the generated APK explicitly:

```bash
python3 install.py --serial TV_IP:5555 --apk build/jellyfish-drift.apk
```

The script uses ADB's package installer. It does not change the TV's selected screensaver, sleep timers, power controls, or system update settings. Choose Jellyfish Drift afterward in the device's screensaver or ambient display settings. Availability and menu names vary by vendor and Android version.

To remove it interactively:

```bash
python3 install.py --serial TV_IP:5555 --uninstall
```

Removal deletes the app and its local preferences. Non-interactive removal requires `--uninstall --yes --force`. Do not use removal as an update; the installer uses `adb install -r` to preserve local settings.

## Amazon Fire TV behavior

The app cannot prevent Fire OS from replacing or disabling applications, prevent device firmware updates, or override system sleep behavior. Fire TV Toolkit provides separate, reversible device controls; use its documented commands if needed and record the existing device values before changing them. Jellyfish Drift itself does not change those system settings.
