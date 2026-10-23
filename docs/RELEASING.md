# Releasing

Use SemVer. The current candidate is 3.0.0/code 7. Publish only after device checks, coverage, documentation, repository metadata, and CI are complete. Increment the Android version code for every published APK and keep the package ID and signing certificate stable.

Before tagging, run the full build and test suite, verify the signed APK package, DreamService metadata, permissions, certificate and SHA-256. Attach the APK and matching `.sha256` file to the GitHub release. Release notes must state compatibility, new settings, behavior changes, commands, and the exact upgrade path. Do not replace artifacts under an existing tag.

Publish only from the verified remote primary branch with its matching semantic version tag. Compare the rebuilt unsigned payload with the exact device-tested candidate, retain its protected signing identity, and verify the actual public download plus checksum. Historical tags and release assets remain immutable.
