# Testing and coverage

Run the host option tests and the JaCoCo coverage gate:

```bash
bash test.sh
bash scripts/test-python-coverage.sh
bash scripts/test-coverage.sh
```

Coverage measures the standalone installer and pure, project-owned settings resolution and validation classes. Both line and branch coverage must be 100%. Android framework lifecycle and drawing code are validated through APK inspection and device checks; they are not excluded from a claimed percentage because they are not in the host coverage source set.

Build and inspect the app with:

```bash
bash build.sh
python3 install.py --serial TV_IP:5555 --apk build/jellyfish-drift.apk
```

Verify the actual DreamService activation, D-pad settings, setting persistence, preview, pause/resume, back/exit, and uninstall on each target. Use an explicit ADB serial and restore any device settings changed during verification. Record hardware, OS/API, display resolution, and build hash in [device verification](VERIFICATION.md).
