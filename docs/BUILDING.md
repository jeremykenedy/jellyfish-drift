# Building

## Requirements

- JDK 17 or later.
- Android SDK platform 36 and build-tools 36.0.0.
- `aapt2`, `javac`, `d8`, `zipalign`, `apksigner`, `keytool`, and OpenSSL.

Set `ANDROID_HOME` if the SDK is not at `~/Library/Android/sdk`. Then run:

```bash
bash build.sh
```

The APK and SHA-256 file are written to `build/`. The build requires the original signing key and password under `~/.android/`. It fails if either is missing rather than generating an incompatible replacement. Back up both files securely and never commit them. Set `JELLYFISH_KEYSTORE` and `JELLYFISH_KEYPASS` to use an existing protected key. CI explicitly creates disposable runner credentials; its APKs are validation artifacts, not production updates.

The isolated filmed candidate defaults to `2.0.0` and version code `4`, above the previous code `3` filmed candidate. Use `VERSION_NAME` and `VERSION_CODE` only for a deliberately versioned build. Validate the final signed APK and hash together before publishing. See [release process](RELEASING.md).

To install a local development build, use `python3 install.py --serial TV_IP:5555 --apk build/jellyfish-drift.apk`. Without `--apk`, the installer downloads the latest published release and verifies its checksum.
