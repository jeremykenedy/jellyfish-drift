# Installation, update, and removal

Install the signed APK from a verified release. If installing from GitHub, compare the downloaded APK with its published SHA-256 file before installing. The app does not install or select other screensavers.

For local installation, build the APK and run:

```bash
python3 install.py --serial TV_IP:5555
```

The script uses ADB's package installer. It does not change the TV's selected screensaver, sleep timers, power controls, or system update settings. Choose Jellyfish Drift afterward in the device's screensaver or ambient display settings. Availability and menu names vary by vendor and Android version.

To remove it interactively:

```bash
python3 install.py --serial TV_IP:5555 --uninstall
```

Removal deletes the app and its local preferences. Non-interactive removal requires both `--uninstall --yes --force`. Do not use removal as an update; `adb install -r` preserves the package's local data when the same signing key is used.

## Amazon Fire TV behavior

The app cannot prevent Fire OS from replacing or disabling applications, prevent device firmware updates, or override system sleep behavior. Fire TV Toolkit provides separate, reversible device controls; use its documented commands if needed and record the existing device values before changing them. Jellyfish Drift itself does not change those system settings.
