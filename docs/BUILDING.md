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

The classic restoration defaults to `3.0.0`, Android code `5`, above every prior installed version code. Preserve the original signing key and package for a data-preserving update. See [release process](RELEASING.md).
