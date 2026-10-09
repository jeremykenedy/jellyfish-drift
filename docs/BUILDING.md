# Building

## Requirements

- JDK 17 or later.
- Android SDK platform 36 and build-tools 36.0.0.
- `aapt2`, `javac`, `d8`, `zipalign`, `apksigner`, `keytool`, and OpenSSL.

Set `ANDROID_HOME` if the SDK is not at `~/Library/Android/sdk`. Then run:

```bash
bash build.sh
```

The APK and SHA-256 file are written to `build/`. The first local build creates a unique signing key and password under `~/.android/`. Back up both files securely; keep this key for all future signed updates. Never commit them. Set `JELLYFISH_KEYSTORE` and `JELLYFISH_KEYPASS` to use an existing protected key. CI builds use disposable runner credentials and those APKs are not production releases.

Use `VERSION_NAME` and `VERSION_CODE` to override the defaults for a release build. Validate the final signed APK and hash together before publishing. See [release process](RELEASING.md).

To install a local development build, use `python3 install.py --serial TV_IP:5555 --apk build/jellyfish-drift.apk`. Without `--apk`, the installer downloads the latest published release and verifies its checksum.
