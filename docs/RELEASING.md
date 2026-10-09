# Releasing

Use SemVer. The first production release should be `1.0.0` only after device checks, coverage, documentation, repository metadata, and CI are complete. Increment the Android version code for every published APK and keep the package ID and signing certificate stable.

Before tagging, run the full build and test suite, verify the signed APK package, DreamService metadata, permissions, certificate and SHA-256. Attach the APK and matching `.sha256` file to the GitHub release. Release notes must state compatibility, new settings, behavior changes, commands, and the exact upgrade path. Do not replace artifacts under an existing tag.
