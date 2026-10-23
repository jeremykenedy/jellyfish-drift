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

## Native provider persistence regression

After installing the candidate on an exclusively owned, authorized test device, run:

```bash
python3 tests/native_provider_persistence.py --serial emulator-5580 --cycles 6 --output build/native-provider-persistence
```

The helper changes only Jellyfish Drift's visual preferences and force-stops its process. It tests write-then-process-death and confirmed-process-death-then-write, compares all six raw saved values, captures command responses, and restores the original six values in cleanup. It does not install an APK, change Dream selection, request permissions, or edit OS settings. Run it before Preview and Dream quality gates. A failed case stays failed even if cleanup succeeds.

This is an Android process and persistence check, not a host test or a performance benchmark. A physical comparison passed four cases each on code 3 and code 4, without reproducing the earlier dark-transition failure; see [verification](VERIFICATION.md).
