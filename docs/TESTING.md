# Testing and coverage

```sh
bash test.sh
bash scripts/test-python-coverage.sh
bash scripts/test-coverage.sh
python3 scripts/check-privacy.py
bash build.sh
```

The Java gate measures JellyfishOptions, SettingsValues and JellyfishGeometry. It requires 100 percent line and branch coverage with JaCoCo COVEREDRATIO rules. Geometry tests verify closed bounded bell coordinates, separate ribbon edges and attachment angles, exact indexed reconstruction of every original triangle/parameter bit, signed zero and the full 16-bit index range; settings tests exercise explicit, random and unknown persisted choices. The standalone installer has its own 100 percent line and branch gate, including download, checksum, redirect, device selection and removal failures.

The old COVEREDCOUNT minimum of one line and branch did not enforce the documented percentage. The candidate corrects that rule. It also moves the Gradle project to the repository root: the former Spotless target globs matched no source files from coverage/. The current formatter targets actual Java sources and runs as part of the required check. Reports are under build/coverage-gradle/reports/.

Host coverage does not include Android framework lifecycle, GPU shader execution or rendered image quality. They are separate native gates, not covered by the percentage above. Inspect the signed APK manifest, bundled license/shaders and certificate, then test the actual Preview and DreamService, remote settings and credits, pause/resume, Back/Home and preserved preferences on each target. Always use an explicit ADB serial and restore the exact initial app and system values.

The first native candidate exposed a shared shader uniform precision mismatch despite a successful Java build. That candidate was rejected. Native shader compilation and visible output remain mandatory before publication. See [verification](VERIFICATION.md) for current candidate status and historical evidence.
